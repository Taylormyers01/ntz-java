package rocks.zipcode;



import java.io.IOException;
import java.security.Security;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ntz main command.
 */
public final class Notez {

    private FileMap filemap;
    Logger logger = Logger.getLogger("Mylogger");
    FileHandler fh;





    public Notez() {
        this.filemap  = new FileMap();
    }
    /**
     * Says hello to the world.
     *
     * @param args The arguments of the program.
     */
    public static void main(String argv[]) {

        boolean _debug = true;
        // for help in handling the command line flags and data!
        if (_debug) {
            System.err.print("Argv: [");
            for (String a : argv) {
                System.err.print(a+" ");
            }
            System.err.println("]");
        }

        Notez ntzEngine = new Notez();
        ntzEngine.loggerSetup();
        ntzEngine.loadDatabase();


        /*
         * You will spend a lot of time right here.
         *
         * instead of loadDemoEntries, you will implement a series
         * of method calls that manipulate the Notez engine.
         * See the first one:
         */
        //ntzEngine.loadDemoEntries();



        if (argv.length == 0) { // there are no commandline arguments
            //just print the contents of the filemap.
            ntzEngine.printResults();
        } else {
            if (argv[0].equals("-r")) {
                ntzEngine.addToCategory("General", argv);
                //logger.info(ntzEngine.printResults());
            }
            else if(argv[0].equals("-c")) {
                if (ntzEngine.filemap.containsKey(argv[1])) {
                    String[] hold = new String[argv.length - 2];
                    int position = 0;
                    for (int i = 2; i < argv.length; i++) {
                        hold[position] = argv[i];
                        position++;
                    }
                    ntzEngine.addToCategory(argv[1], hold);
                    ntzEngine.printResults();
                }
            }
            else if(argv[0].equals("-f")){
                if(ntzEngine.filemap.containsKey(argv[1])){
                    ntzEngine.filemap.remove(argv[1], Integer.parseInt(argv[2]));
                    ntzEngine.printResults();
                }
                else{
                    System.out.println("Invalid input");
                }
            }
            else if(argv[0].equals("-e")){
                if(ntzEngine.filemap.containsKey(argv[1])) {
                    String newValue = "";
                    for(int i = 3; i < argv.length; i++){
                        newValue = newValue + argv[i] + " ";
                    }
                    ntzEngine.filemap.replace(argv[1], Integer.parseInt(argv[2]), newValue);

                }
            }
        }
            // this should give you an idea about how to TEST the Notez engine
              // without having to spend lots of time messing with command line arguments.

        /*
         * what other method calls do you need here to implement the other commands??
         */
        ntzEngine.saveDatabase();

    }

    private void addToCategory(String string, String[] argv) {
        StringBuilder sb = new StringBuilder();

        for(String s: argv){
            if(!s.equalsIgnoreCase("-r")){
                sb.append(s + " ");
            }
        }
        logger.info(sb + " Added to " + string );
        //System.out.println(sb);
        filemap.get(string).add(sb.toString());
    }

    private void saveDatabase() {
        filemap.save();
    }

    private void loadDatabase() {
        filemap.load();
    }

    public void printResults() {
        System.out.println(this.filemap.printFileMap());
    }

    public void loadDemoEntries() {
        filemap.put("General", new NoteList("The Very first Note"));
        filemap.put("General", new NoteList("The Very second Note"));
        filemap.put("note2", new NoteList("A secret second note"));
        filemap.put("category3", new NoteList("Did you buy bread AND eggs?"));
        filemap.put("anotherNote", new NoteList("Hello from ZipCode!"));
    }
    /*
     * Put all your additional methods that implement commands like forget here...
     */
    public void loggerSetup(){
        try {
            try {
                fh = new FileHandler("MyLog.log");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            logger.addHandler(fh);
        } catch (SecurityException e){
            e.printStackTrace();
        }
        logger.setLevel(Level.INFO);
    }

}
