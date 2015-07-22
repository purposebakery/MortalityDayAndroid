package de.techlung.android.mortalityday.greendao;

import java.io.File;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MortalityDayDaoGenerator {
    public static final int DATABASE_VERSION = 1;

    private MortalityDayDaoGenerator() {

    }
    /**
     * Some Entitys has 'Keep Sections'. For Example methods that are note
     * generated.
     * We have to copy the generated Files to an other location, otherwise
     * the 'keep sections' will be deleted.
     */
    public static void main(String[] args) throws Exception {

        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        new File("de/techlung/android/mortalityday/greendao/generated").mkdirs();
        System.out.println("##################  copy files from src ##################");
        moveFiles("app/src/main/java/de/techlung/android/mortalityday/greendao/generated", "de/techlung/android/mortalityday/greendao/generated/");
        File f = new File("app/src/main/java/de/techlung/android/mortalityday/greendao/generated");
        deleteDir(f);
        new File("app/src/main/java/de/techlung/android/mortalityday/greendao/generated").mkdirs();
        System.out.println("##################  Generating DAO's ##################");
        generateDaos();
        System.out.println("##################  copy files to src ##################");
        moveFiles("de/techlung/android/mortalityday/greendao/generated", "app/src/main/java/de/techlung/android/mortalityday/greendao/generated/");
        System.out.println("##################       clean        ##################");
        deleteDir(new File("de"));
        System.out.println("##################       done!        ##################");
    }

    private static void generateDaos() throws Exception {
        Schema schema = new Schema(DATABASE_VERSION, "de.techlung.android.mortalityday.greendao.generated");

        // ISO XML
        Entity thought = createThoughtEntity(schema);
        Entity thoughtmeta = createThoughtMetaEntity(schema);

        new DaoGenerator().generateAll(schema, ".");
    }

    private static Entity createThoughtEntity(Schema schema) {
        Entity thought = schema.addEntity("Thought");
        thought.setHasKeepSections(true);

        thought.addStringProperty("key").unique().primaryKey();
        thought.addIntProperty("category");
        thought.addStringProperty("text");
        thought.addIntProperty("rating");
        thought.addBooleanProperty("shared");

        return thought;
    }

    private static Entity createThoughtMetaEntity(Schema schema) {
        Entity thought = schema.addEntity("ThoughtMeta");
        thought.setHasKeepSections(true);

        thought.addStringProperty("key").unique().primaryKey();
        thought.addBooleanProperty("wasVoted");

        return thought;
    }


    // Utilities
    private static void moveFiles(String fromPath, String toPath) {
        File fromDirectory = new File(fromPath);

        for (File from : fromDirectory.listFiles()) {
            File to = new File(toPath + from.getName());
            System.out.print("Moving: ");
            System.out.print(from.getAbsolutePath());
            System.out.print("  -->  ");
            System.out.println(to.getAbsolutePath());
            from.renameTo(to);
        }
    }

    private static void deleteDir(File path) {
        for (File file : path.listFiles()) {
            if (file.isDirectory()) {
                deleteDir(file);
            }
            System.out.println("Deleting: " + file.getAbsolutePath());
            file.delete();
        }
        System.out.println("Deleting: " + path.getAbsolutePath());
        path.delete();
    }

}
