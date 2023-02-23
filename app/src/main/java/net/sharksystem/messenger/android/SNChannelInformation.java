package net.sharksystem.messenger.android;

import net.bytebuddy.asm.Advice;

@Deprecated
class SNChannelInformation {
    final CharSequence uri;
    final CharSequence name;

    SNChannelInformation(CharSequence uri, CharSequence name) {
        this.uri = uri;
        this.name = name;
    }
}
