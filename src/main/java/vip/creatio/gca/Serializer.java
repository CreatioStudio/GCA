package vip.creatio.gca;

import vip.creatio.gca.util.ByteVector;

public interface Serializer {

    int byteSize();

    void serialize(ByteVector buffer);

}
