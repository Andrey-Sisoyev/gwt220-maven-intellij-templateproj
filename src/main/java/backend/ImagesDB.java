package backend;

import java.util.concurrent.ConcurrentHashMap;

import middle.ImageType;

import backend.entities.ImageEntity;

public class ImagesDB {
    public static class IMAGES_SEQ{
        private static volatile int curID = 1;
        public static synchronized int next() { return curID++; }
    }

    public static final ConcurrentHashMap<Integer, ImageEntity> IMAGES = new ConcurrentHashMap<Integer, ImageEntity>();
    static {
        Integer id;
        
        id = IMAGES_SEQ.next();
        IMAGES.put(
                  id
                , new ImageEntity(
                        id
                      , "http://th02.deviantart.net/fs71/PRE/f/2011/117/b/e/orizzonte_degli_eventi_by_lucarduca-d379132.jpg"
                      , "Biokosmos"
                      , ImageType.NET
                      , 777777
                      , true
                      )
        );
        
        id = IMAGES_SEQ.next();
        IMAGES.put(
                id
              , new ImageEntity(
                      id
                    , "http://th04.deviantart.net/fs70/PRE/f/2011/075/6/a/unidentified_satellite_by_noisecraft-d3bsamp.jpg"
                    , "Krugovertj"
                    , ImageType.NET
                    , 777777
                    , true
                    )
        );
        
        id = IMAGES_SEQ.next();
        IMAGES.put(
                id
              , new ImageEntity(
                      id
                    , "http://th05.deviantart.net/fs71/PRE/i/2011/127/3/5/alps_by_chloeleedwards-d3fte8k.jpg"
                    , "Alpi"
                    , ImageType.NET
                    , 777777
                    , true
                    )
        );
        
        id = IMAGES_SEQ.next();
        IMAGES.put(
                id
              , new ImageEntity(
                      id
                    , "http://th05.deviantart.net/fs10/PRE/i/2006/131/d/c/Novus_Natura__Complete_by_bentolman.jpg"
                    , "Biokosmos2"
                    , ImageType.NET
                    , 777777
                    , true
                    )
        );
        
        id = IMAGES_SEQ.next();
        IMAGES.put(
                id
              , new ImageEntity(
                      id
                    , "http://fc06.deviantart.net/fs71/i/2011/121/4/a/in_your_face_by_catchthewind213-d3fcno0.jpg"
                    , "..."
                    , ImageType.NET
                    , 777777
                    , true
                    )
        );
        
        id = IMAGES_SEQ.next();
        IMAGES.put(
                id
              , new ImageEntity(
                      id
                    , "http://th05.deviantart.net/fs71/PRE/i/2011/127/3/5/alps_by_chloeleedwards-d3fte8k.jpg"
                    , "Alpi"
                    , ImageType.NET
                    , 777777
                    , true
                    )
        );
        
        id = IMAGES_SEQ.next();
        IMAGES.put(
                id
              , new ImageEntity(
                      id
                    , "http://th05.deviantart.net/fs71/PRE/i/2011/127/3/5/alps_by_chloeleedwards-d3fte8k.jpg"
                    , "Alpi"
                    , ImageType.NET
                    , 777777
                    , true
                    )
        );
        
        id = IMAGES_SEQ.next();
        IMAGES.put(
                id
              , new ImageEntity(
                      id
                    , "http://th05.deviantart.net/fs71/PRE/i/2011/127/3/5/alps_by_chloeleedwards-d3fte8k.jpg"
                    , "Alpi"
                    , ImageType.NET
                    , 777777
                    , true
                    )
        );
        
        id = IMAGES_SEQ.next();
        IMAGES.put(
                id
              , new ImageEntity(
                      id
                    , "http://th05.deviantart.net/fs71/PRE/i/2011/127/3/5/alps_by_chloeleedwards-d3fte8k.jpg"
                    , "Alpi"
                    , ImageType.NET
                    , 777777
                    , true
                    )
        );
        
        id = IMAGES_SEQ.next();
        IMAGES.put(
                id
              , new ImageEntity(
                      id
                    , "http://th05.deviantart.net/fs71/PRE/i/2011/127/3/5/alps_by_chloeleedwards-d3fte8k.jpg"
                    , "Alpi"
                    , ImageType.NET
                    , 777777
                    , true
                    )
        );
        
        id = IMAGES_SEQ.next();
        IMAGES.put(
                id
              , new ImageEntity(
                      id
                    , "http://th05.deviantart.net/fs71/PRE/i/2011/127/3/5/alps_by_chloeleedwards-d3fte8k.jpg"
                    , "Alpi"
                    , ImageType.NET
                    , 777777
                    , true
                    )
        );
        
        id = IMAGES_SEQ.next();
        IMAGES.put(
                id
              , new ImageEntity(
                      id
                    , "http://th05.deviantart.net/fs71/PRE/i/2011/127/3/5/alps_by_chloeleedwards-d3fte8k.jpg"
                    , "Alpi"
                    , ImageType.NET
                    , 777777
                    , true
                    )
        );

        id = IMAGES_SEQ.next();
        IMAGES.put(
                id
              , new ImageEntity(
                      id
                    , "http://fc02.deviantart.net/fs70/f/2011/149/3/e/dandelion_by_haryarti-d3hhx8y.jpg"
                    , "Oduvan4ik"
                    , ImageType.NET
                    , 777777
                    , true
                    )
        );


    }
    // ================================
    // NON-STATIC STUFF

    // ================================
    // CONSTRUCTORS
    private ImagesDB() {}

    // ================================
    // GETTERS/SETTERS

    // ================================
    // METHODS

    // ================================
    // LOW-LEVEL OVERRIDES

}
