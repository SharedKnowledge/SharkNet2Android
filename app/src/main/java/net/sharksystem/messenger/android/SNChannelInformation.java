package net.sharksystem.messenger.android;

@Deprecated
class SNChannelInformation {
    final CharSequence uri;
    final CharSequence name;

    SNChannelInformation(CharSequence uri, CharSequence name) {
        this.uri = uri;
        this.name = name;
    }
}
