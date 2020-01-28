package Models;

public class Movie {

    private String nameOfMovie;
    private String poster;
    private String releaseDate;
    private String plotSynopsis;
    private String userRating;

    public Movie(){

    }

    public Movie(String nameOfMovie, String poster, String releaseDate, String plotSynopsis, String userRating){
        this.nameOfMovie = nameOfMovie;
        this.poster = poster;
        this.releaseDate = releaseDate;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
    }

    public String getNameOfMovie(){
        return nameOfMovie;
    }

    public void setNameOfMovie(String nameOfMovie){
        this.nameOfMovie = nameOfMovie;
    }

    public String getPoster(){
        return poster;
    }

    public void setPoster(String poster){
        this.poster = poster;
    }

    public String getReleaseDate(){
        return releaseDate;

    }

    public void setReleaseDate(String releaseDate){
        this.releaseDate = releaseDate;
    }

    public String getPlotSynopsis(){
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getUserRating(){
        return userRating;
    }

    public void setUserRating(String userRating){
        this.userRating = userRating;
    }

}
