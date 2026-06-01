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
        jint leader) {

    Deal dl;
    dl.trump = trump;
    dl.first = leader;

    for (int i = 0; i < 3; i++) {
        dl.currentTrickSuit[i] = 0;
        dl.currentTrickRank[i] = 0;
    }

    jint *cardArray = env->GetIntArrayElements(cards, nullptr);
    for (int hand = 0; hand < 4; hand++) {
        for (int suit = 0; suit < 4; suit++) {
            dl.remainCards[hand][suit] = cardArray[hand * 4 + suit];
        }
    }
    env->ReleaseIntArrayElements(cards, cardArray, 0);

    FutureTricks ft;

    int result = SolveBoard(dl, -1, 1, 1, &ft, 0);

    if (result != RETURN_NO_FAULT) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "DDS error: %d", result);
        return -1;
    }

    return ft.suit[0] * 100 + ft.rank[0];
}

JNIEXPORT jintArray JNICALL
Java_com_example_bridge_DdsSolver_calcFullDDTable(
        JNIEnv *env, jobject thiz,
        jintArray cards) {

    DdTableDeal tableDeal;

    jint *cardArray = env->GetIntArrayElements(cards, nullptr);
    for (int hand = 0; hand < 4; hand++) {
        for (int suit = 0; suit < 4; suit++) {
            tableDeal.cards[hand][suit] = cardArray[hand * 4 + suit];
        }
    }
    env->ReleaseIntArrayElements(cards, cardArray, 0);

    DdTableResults tableRes;
    int result = CalcDDtable(tableDeal, &tableRes);

    if (result != RETURN_NO_FAULT) {
        __android_log_print(ANDROID_LOG_ERROR, TAG, "DDS error: %d", result);
        jintArray empty = env->NewIntArray(20);
        return empty;
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
