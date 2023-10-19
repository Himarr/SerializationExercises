package org.example;
import com.google.gson.GsonBuilder;
import org.example.model.*;

import com.google.gson.Gson;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.reflect.TypeToken;

public class SerializationExercises {
    /*
        Should define the class for the concepts Movie, Theater and Session.
        A session is a play of movie in a theater.
        Create 2 instances of each class and relate among them.
        Serialize to Json all objects and save then in different files.
     */
    public static class Exercise1 {
        public static void main(String[] args) {
            // 1. Create instances to serialize
            List<Movie> movies = List.of(new Movie("Oppenheimer", 180),
                    new Movie("Test", 150));
            List<Theater> theaters = List.of(new Theater("Theater1", 20),
                    new Theater("Theater2", 50));
            List<Session> sessions = List.of(new Session(movies.get(0), theaters.get(0)),
                    new Session(movies.get(1), theaters.get(1)));

            // 2. Set Up Gson Builder
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // 3. Serialize Objects to gson
            serializeObjects(movies, "movie.json", gson);
            serializeObjects(theaters, "theater.json", gson);
            serializeObjects(sessions, "session.json", gson);
        }

        private static void serializeObjects(List<?> objects, String fileName, Gson gson) {
            try (FileWriter writer = new FileWriter(fileName)) {
                gson.toJson(objects, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
        Deserialize the objects created in exercise 1.
        Now serialize them using ObjectOutputStream
     */
    public static class Exercise2 {

        public static void main(String[] args) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // Deserialize objects
            List<Movie> movies = deserializeObject("movie.json", gson, Movie.class);
            List<Theater> theaters = deserializeObject("theater.json", gson, Theater.class);
            List<Session> sessions = deserializeObject("session.json", gson, Session.class);

            // Serialize to binary
            serializeToBinary(movies, "movies.bin");
            serializeToBinary(theaters, "theaters.bin");
            serializeToBinary(sessions, "sessions.bin");
        }

        private static <T> List<T> deserializeObject(String filename, Gson gson, Class<T> classOfT) {
            try (FileReader reader = new FileReader(filename)) {
                Type listType = TypeToken.getParameterized(List.class, classOfT).getType();
                return gson.fromJson(reader, listType);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private static void serializeToBinary(List<?> objects, String filename) {
            try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(filename))) {
                stream.writeObject(objects);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /*
       Deserialize the objects from the binary files created in exercise 2.
    */
    public static class Exercise3 {

        public static void main(String[] args) {
            List<Movie> movies = deserializeFromBinary("movies.bin");
            List<Theater> theaters = deserializeFromBinary("theaters.bin");
            List<Session> sessions = deserializeFromBinary("sessions.bin");
        }

        private static <T> List<T> deserializeFromBinary(String filename) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filename))) {
                List<?> deserializedList = (List<?>) objectInputStream.readObject();
                return (List<T>) deserializedList;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}

