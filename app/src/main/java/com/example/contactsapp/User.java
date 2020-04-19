package com.example.contactsapp;

class User {

    String name;
    String contacts;

    public User(String name, String contacts) {
        this.name = name;
        this.contacts = contacts;
    }

    public String getName() {
        return name;
    }

    public String getContacts() {
        return contacts;
    }
}
