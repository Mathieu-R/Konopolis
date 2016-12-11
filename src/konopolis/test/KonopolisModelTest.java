package src.konopolis.test;

import org.junit.Test;
import src.konopolis.model.KonopolisModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class KonopolisModelTest {

    private KonopolisModel model;

    public KonopolisModelTest() {
        this.model = new KonopolisModel();
    }

    @Test
    public void retrieveOrCreateGenreId() throws Exception {
        int genre_id = model.retrieveOrCreateGenreId("Action");
        show("" + genre_id);
        assertEquals(6, genre_id);
    }

    @Test
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

    @Test
    public void retrieveAllMoviesTitles() throws Exception {
        final HashMap<Integer, String> moviesList = model.retrieveAllMoviesTitles(); // retrieve all the titles
        moviesList.forEach((key, value) -> {
            show(key + " => " + value);
            //AssertEq
        });
    }

    @Test
    public void retrieveMovie() throws Exception {

    }

    @Test
    public void retrieveRoom() throws Exception {

    }

    @Test
    public void retrieveAllRooms() throws Exception {

    }

    @Test
    public void retrieveCustomers() throws Exception {

    }

    @Test
    public void retrieveTypes() throws Exception {

    }

    @Test
    public void retrieveCustomerTypeId() throws Exception {

    }

    @Test
    public void retrieveMovieRoomId() throws Exception {

    }

    @Test
    public void retrieveLanguageId() throws Exception {

    }



    @Test
    public void retrieveOrCreateCastId() throws Exception {

    }

    @Test
    public void retrieveMovieId() throws Exception {

    }

    @Test
    public void addGenres() throws Exception {

    }

    @Test
    public void addShows() throws Exception {

    }

    @Test
    public void addCasting() throws Exception {

    }

    @Test
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