package Models;

public class Reviews {
    private String author;
    private String content;

    public Reviews(){

    }

    public Reviews(String author, String content){
        this.author = author;
        this.content = content;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public String getAuthor(){
        return author;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getContent(){
        return content;
    }

}
