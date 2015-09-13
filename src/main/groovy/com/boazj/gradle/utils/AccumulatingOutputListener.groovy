package com.boazj.gradle.utils

class AccumulatingOutputListener implements OutputListener {

    private def String acc = ''

    @Override
    void onOutput(String s, Color color) {
        acc += s
    }

    boolean contains(String s) {
        return acc.contains(s)
    }
}
