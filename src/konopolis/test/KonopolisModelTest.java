package konopolis.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import konopolis.model.KonopolisModel;

public class KonopolisModelTest {

private KonopolisModel model;

    public KonopolisModelTest() {
        this.model = new KonopolisModel();
    }


    public void retrieveOrCreateGenreId() throws Exception {
        int genre_id = model.retrieveOrCreateGenreId("Action");
        show("" + genre_id);
        assertEquals(26, genre_id);
    }


    public void addMovie() throws Exception {
        // Description
        String description = "Samba, sénégalais en France depuis 10 ans, collectionne les petits boulots ;" +
                " Alice est une cadre supérieure épuisée par un burn out." +
                " Lui essaye par tous les moyens d'obtenir ses papiers," +
                " alors qu'elle tente de se reconstruire par le bénévolat dans une association." +
                " Chacun cherche à sortir de son impasse jusqu'au jour où leurs destins se croisent... " +
                "Entre humour et émotion, leur histoire se fraye un autre chemin vers le bonheur. " +
                "Et si la vie avait plus d'imagination qu'eux ?";
        // Shows
        LocalDateTime show1 = makeDate(19, 12, 2016, 15, 30);
        LocalDateTime show2 = makeDate(20,12, 2016,17,15);
        ArrayList<LocalDateTime> shows = new ArrayList<LocalDateTime>();
        shows.add(show1);
        shows.add(show2);
        // Casting
        ArrayList<String> casting = new ArrayList<String>();
        casting.add("Omar Sy");
        casting.add("Charlotte Gainsbourg");
        casting.add("Tahar Rahim");
        // genres
        ArrayList<String> genres = new ArrayList<String>();
        genres.add("Comédie");
        genres.add("Drame");
        // ADD MOVIE METHOD
        model.addMovie(2, "Samba", description, "Eric Toledano", shows, casting, 118, "Français", 10, genres);
    }


    public void retrieveAllMoviesTitles() throws Exception {
        final LinkedHashMap<Integer, String> moviesList = model.retrieveAllMoviesTitles(); // retrieve all the titles
        moviesList.forEach((key, value) -> {
            show(key + " => " + value);
            // Some test later
        });
    }


    public void retrieveMovie() throws Exception {
        assertEquals("Samba",model.retrieveMovie(1).getTitle());
        model.getMovies_al().forEach(movie -> {
            if (movie.getId() == 1) {
                assertEquals("Samba", movie.getTitle());
            }
        });
    }


    public void retrieveRoom() throws Exception {
        LocalDateTime show = makeDate(19, 12, 2016, 15, 30);
        assertEquals("Samba", model.retrieveRoom(1,1, show).getMovie().getTitle());
        assertEquals(450, model.retrieveRoom(1,1, show).getTotSeats());
    }


    public void retrieveAllRooms() throws Exception {

    }


    public void retrieveCustomers() throws Exception {

    }


    public void retrieveTypes() throws Exception {

    }


    public void retrieveCustomerTypeId() throws Exception {

    }


    public void retrieveMovieRoomId() throws Exception {

    }


    public void retrieveLanguageId() throws Exception {

    }




    public void retrieveOrCreateCastId() throws Exception {

    }


    public void retrieveMovieId() throws Exception {

    }


    public void addGenres() throws Exception {

    }


    public void addShows() throws Exception {

    }


    public void addCasting() throws Exception {

    }


    public void addCustomer() throws Exception {

    }

    /**
     * Create a Date object from a day, month, year, hours and minutes
     * month - 1 because month is "0 based" so it begins from 0 but the user begin by 1.
     * @return LocalDateTime, a LocalDateTime constructed from the parameters passed in the function
     */
    private LocalDateTime makeDate(int day, int month, int year, int hours, int minutes) {
        return LocalDateTime.of(year, month, day, hours, minutes);
    }

    private void show(String str) {
        System.out.println(str);
    }
}