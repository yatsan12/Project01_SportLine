package model;

public class User {

    private String id,username,imageURL,status ;

    public User(){

    }

    public User(String username,String id,String imageURL,String status){
        this.username=username;
        this.id=id;
        this.imageURL=imageURL;
        this.status=status;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id=id;
    }

    public String getImageURL(){
        return imageURL;
    }

    public void setImageURL(String imageURL){
        this.imageURL=imageURL;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status=status;
    }

}
