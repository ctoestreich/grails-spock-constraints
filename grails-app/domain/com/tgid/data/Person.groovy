package com.tgid.data

class Person {

    static hasMany = [children: Person]

    String firstName
    String middleName
    String lastName
    String email
    Integer age
    String ssn
    String amex
    String gender
    String login
    Date birthDate
    Float wage
    Integer fingers
    String homePage
    String username

    static constraints = {
        firstName size: 1..50
        middleName size: 0..50
        lastName size: 1..50
        email email: true, notEqual: "bill@microsoft.com"
        age nullable: false, range: 0..150
        ssn unique: true, blank: false
        amex creditCard: true
        gender inList: ["Male", "Female"]
        login matches: "[a-zA-Z]+"
        birthDate max: new Date()
        wage min: 0F, scale: 2
        children maxSize: 10, minSize: 2
        fingers nullable: true
        homePage url: true
        username validator: {
            if(!it.startsWith('boba')) return ['invalid.bountyhunter']
        }

    }
}
