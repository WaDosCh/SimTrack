package ch.awae.simtrack.util.serial;

import java.io.IOException;
import java.io.ObjectOutputStream;

public interface CustomSerialization extends CustomDeserialization {

	void writeObject(ObjectOutputStream out) throws IOException;

}
