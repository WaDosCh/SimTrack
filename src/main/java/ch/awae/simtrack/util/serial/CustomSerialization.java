package ch.awae.simtrack.util.serial;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface CustomSerialization {

	void writeObject(ObjectOutputStream out) throws IOException;

	void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException;

}
