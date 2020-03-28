package Models;

public class Trailer {

    private String Id;
    private String name;
    private String key;

    public Trailer(){

    }

    public void setId(String id){
        this.Id = id;

    }

    public String getId(){
        return Id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setKey(String key){
        this.key = key;
    }

    public String getKey(){
        return key;
    }

}
