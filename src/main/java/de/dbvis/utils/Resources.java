package de.dbvis.utils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wolfgang Jentner.
 */
public class Resources {

    /**
     * Returns the Path for a given resource path
     * @param path the resource to read
     * @return a Path that represents the resource
     * @throws URISyntaxException in case the URI is malformed.
     */
    public static Path getResource(String path) throws URISyntaxException {
        URI uri = Resources.class.getClassLoader().getResource(path).toURI();
        return Paths.get(uri);
    }

    /**
     * Lists all the files & subfolders contained in a given resource folder.
     * @param path the folder to scan
     * @return returns a list of files
     * @throws IOException if the parent folder does not exist or can't be read
     */
    public static List<File> getResourceFiles(String path) throws IOException, URISyntaxException {
        List<File> filenames = new ArrayList<>();
        InputStream in = Resources.class.getClassLoader().getResourceAsStream(path);
        BufferedReader br = new BufferedReader( new InputStreamReader( in ) );
        String resource;
        if(!path.endsWith("/")) {
            path += "/";
        }
        while( (resource = br.readLine()) != null ) {
            filenames.add(Resources.getResource(path + resource).toFile());
        }
        return filenames;
    }
}
