package com.example.yamenandroidacteen.classes;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
    public class User implements Serializable {

        private String name;
        private String email;
        private String password;
        private String city;
        private String region;
        private String age;

        private int followers;
        private int following;
        private int posts;

        private List<String> savedPosts;


        public User(String name, String email, String password, String city, String region, String age) {
                this.name = name;
                this.email = email;
                this.password = password;
                this.city = city;
                this.region = region;
                this.age = age;
                this.following = 0;
                this.followers = 0;
                this.posts = 0;
                this.savedPosts = new ArrayList<>();
        }

        public int getFollowing() {
            return following;
        }

        public void setFollowing(int following) {
            this.following = following;
        }

        public int getPosts() {
            return posts;
        }

        public void setPosts(int posts) {
            this.posts = posts;
        }


        public int getFollowers() {
            return followers;
        }

        public void setFollowers(int followers) {
            this.followers = followers;
        }


        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void addSavedPost(String postId) {
            // Add the post ID to the list of saved posts
            savedPosts.add(postId);
        }

        public void removeSavedPost(String postId) {
            // Remove the post ID from the list of saved posts
            savedPosts.remove(postId);
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    ", age='" + age + '\'' +
                    ", city='" + city + '\'' +
                    ", region='" + region + '\'' +
                    '}';
        }
    }



