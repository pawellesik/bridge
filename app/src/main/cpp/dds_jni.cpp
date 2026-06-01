#include <jni.h>
#include <android/log.h>
#include "dll.h"

#define TAG "DDS_JNI"

extern "C" {

JNIEXPORT void JNICALL
Java_com_example_bridge_DdsSolver_initDds(JNIEnv *env, jobject thiz) {
    SetMaxThreads(1);
    __android_log_print(ANDROID_LOG_INFO, TAG, "DDS initialized");
}

JNIEXPORT jint JNICALL
Java_com_example_bridge_DdsSolver_calcDDTable(
        JNIEnv *env, jobject thiz,
        jintArray cards,
        jint trump,
        jint leader,
        jintArray trickSuits,
        jintArray trickRanks) {

    Deal dl;
    dl.trump = trump;
    dl.first = leader;

    jint *ts = env->GetIntArrayElements(trickSuits, nullptr);
    jint *tr = env->GetIntArrayElements(trickRanks, nullptr);
    for (int i = 0; i < 3; i++) {
        // DDS expects 0 for empty trick slots, not -1
        dl.currentTrickSuit[i] = (ts[i] < 0) ? 0 : ts[i];
        dl.currentTrickRank[i] = (tr[i] < 2) ? 0 : tr[i];
    }
    env->ReleaseIntArrayElements(trickSuits, ts, JNI_ABORT);
    env->ReleaseIntArrayElements(trickRanks, tr, JNI_ABORT);

    jint *cardArray = env->GetIntArrayElements(cards, nullptr);
    for (int hand = 0; hand < 4; hand++) {
        for (int suit = 0; suit < 4; suit++) {
            // Remove the << 2. Java already provides bit 2 for rank 2, etc.
            dl.remainCards[hand][suit] = (unsigned int)cardArray[hand * 4 + suit];
        }
    }
    env->ReleaseIntArrayElements(cards, cardArray, JNI_ABORT);

    FutureTricks ft;
    // solutions = 1 (optimal card), mode = 0
    int result = SolveBoard(dl, -1, 1, 0, &ft, 0);

    if (result != RETURN_NO_FAULT) {
        char msg[80];
        ErrorMessage(result, msg);
        __android_log_print(ANDROID_LOG_ERROR, TAG, "DDS error %d: %s", result, msg);
        return -result; // Return negative error code
    }

    if (ft.cards > 0) {
        // Return suit * 100 + rank (e.g., Spades Ace = 0 * 100 + 14 = 14)
        return ft.suit[0] * 100 + ft.rank[0];
    }
    return -1000; // No cards found
}

JNIEXPORT jintArray JNICALL
Java_com_example_bridge_DdsSolver_calcFullDDTable(
        JNIEnv *env, jobject thiz,
        jintArray cards) {

    DdTableDeal tableDeal;
    jint *cardArray = env->GetIntArrayElements(cards, nullptr);
    for (int hand = 0; hand < 4; hand++) {
        for (int suit = 0; suit < 4; suit++) {
            tableDeal.cards[hand][suit] = (unsigned int)cardArray[hand * 4 + suit];
        }
    }
    env->ReleaseIntArrayElements(cards, cardArray, JNI_ABORT);

    DdTableResults tableRes;
    int result = CalcDDtable(tableDeal, &tableRes);

    if (result != RETURN_NO_FAULT) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "DDS CalcDDtable error: %d", result);
        return env->NewIntArray(0);
    }

    jintArray output = env->NewIntArray(20);
    jint buf[20];
    for (int trump = 0; trump < 5; trump++) {
        for (int leader = 0; leader < 4; leader++) {
            buf[trump * 4 + leader] = tableRes.res_table[trump][leader];
        }
    }
    env->SetIntArrayRegion(output, 0, 20, buf);
    return output;
}

} // extern "C"
