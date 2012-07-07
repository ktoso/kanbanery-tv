#!/bin/sh
adb devices

#adb uninstall pl.project13.kanbanery.tv
adb install -r target/kanbanery-tv-0.1.apk
