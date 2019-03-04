package net.sharksystem.makan;

import net.sharksystem.aasp.AASPStorage;

import java.io.IOException;
import java.util.List;

import identity.IdentityStorage;
import identity.Person;

public class MakanAndroid extends MakanAASPWrapper {

    public MakanAndroid(CharSequence userFriendlyName, CharSequence uri, AASPStorage aaspStorage,
                        Person owner, IdentityStorage identityStorage) throws IOException {

        super(userFriendlyName, uri, aaspStorage, owner, identityStorage);
    }

    @Override
    public List<Person> getMember() throws IOException {
        throw new IOException("that's a dummy- should be implemeneted in Android version");
    }

    @Override
    public void addMember(Person person) throws MakanException, IOException {
        throw new IOException("that's a dummy- should be implemeneted in Android version");
    }

    @Override
    public void removeMember(Person person2add) throws MakanException, IOException {
        throw new IOException("that's a dummy- should be implemeneted in Android version");

    }

    @Override
    public Person getAdmin() throws MakanException, IOException {
        throw new IOException("that's a dummy- should be implemeneted in Android version");
    }
}