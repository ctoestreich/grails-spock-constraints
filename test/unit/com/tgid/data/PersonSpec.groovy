package com.tgid.data

import grails.test.mixin.TestFor
import spock.lang.Unroll

@TestFor(Person)
class PersonSpec extends ConstraintUnitSpec {

    def setup() {
        //mock a person with some data (put unique violations in here so they can be tested, the others aren't needed)
        mockForConstraintsTests(Person, [new Person(ssn: '123456789')])
    }

    @Unroll({"test person all constraints $field is $error"})
    def "test person all constraints"() {
        when:
        def obj = new Person("$field": val)

        then:
        validateConstraints(obj, field, error)

        where:
        error                  | field        | val
        'size'                 | 'firstName'  | getLongString(51)
        'nullable'             | 'firstName'  | null
        'size'                 | 'middleName' | getLongString(51)
        'nullable'             | 'middleName' | null
        'size'                 | 'lastName'   | getLongString(51)
        'nullable'             | 'lastName'   | null
        'notEqual'             | 'email'      | 'bill@microsoft.com'
        'email'                | 'email'      | getEmail(false)
        'range'                | 'age'        | 151
        'range'                | 'age'        | -1
        'nullable'             | 'age'        | null
        'blank'                | 'ssn'        | ''
        'unique'               | 'ssn'        | '123456789'
        'creditCard'           | 'amex'       | getCreditCard(false)
        'inList'               | 'gender'     | 'Unknown'
        'matches'              | 'login'      | 'ABC123'
        'max'                  | 'birthDate'  | new Date() + 1
        'min'                  | 'wage'       | -1F
        'maxSize'              | 'children'   | createPerson(11)
        'minSize'              | 'children'   | createPerson(1)
        'url'                  | 'homePage'   | getUrl(false)
        'invalid.bountyhunter' | 'username'   | 'buba'
    }

    @Unroll({"person $field is $error using $val"})
    def "test person age constraints"() {
        when:
        def obj = new Person("$field": val)

        then:
        validateConstraints(obj, field, error)

        where:
        error      | field | val
        'range'    | 'age' | 151
        'range'    | 'age' | -1
        'nullable' | 'age' | null
        'valid'    | 'age' | 100
        'valid'    | 'age' | 150
        'valid'    | 'age' | 0
    }

    @Unroll({"person $field is $error using $val"})
    def "test person ssn constraints"() {
        when:
        def obj = new Person("$field": val)

        then:
        validateConstraints(obj, field, error)

        where:
        error      | field | val
        'blank'    | 'ssn' | ''
        'nullable' | 'ssn' | null
        'unique'   | 'ssn' | '123456789'
        'valid'    | 'ssn' | '123456788'
        'valid'    | 'ssn' | '123-45-6787'
    }

    @Unroll({"person $field is $error using $val"})
    def "test person username constraints"() {
        when:
        def obj = new Person("$field": val)

        then:
        validateConstraints(obj, field, error)

        where:
        error                  | field      | val
        'invalid.bountyhunter' | 'username' | ''
        'nullable'             | 'username' | null
        'invalid.bountyhunter' | 'username' | 'bubua'
        'valid'                | 'username' | 'bobafet'
        'valid'                | 'username' | 'bobajunior'
    }

    @Unroll({"person $field is $error using $val"})
    def "test person homepage constraints"() {
        when:
        def obj = new Person("$field": val)

        then:
        validateConstraints(obj, field, error)

        where:
        error   | field      | val
        'url'   | 'homePage' | getUrl(false)
        'valid' | 'homePage' | '' //blanks work for url
        'valid' | 'homePage' | null //null works for url
        'valid' | 'homePage' | getUrl(true) + '/page.gsp'
        'valid' | 'homePage' | getUrl(true)
    }

    @Unroll({"person $field is $error using $val"})
    def "test person gender constraints"() {
        when:
        def obj = new Person("$field": val)

        then:
        validateConstraints(obj, field, error)

        where:
        error      | field    | val
        'inList'   | 'gender' | 'Unknown'
        'nullable' | 'gender' | null
        'valid'    | 'gender' | '' //blanks work for inList
        'valid'    | 'gender' | 'Male'
        'valid'    | 'gender' | 'Female'
    }

    @Unroll({"person $field is $error using $val"})
    def "test person credit card constraints"() {
        when:
        def obj = new Person("$field": val)

        then:
        validateConstraints(obj, field, error)

        where:
        error        | field  | val
        'creditCard' | 'amex' | getCreditCard(false)
        'nullable'   | 'amex' | null
        'valid'      | 'amex' | ''
        'valid'      | 'amex' | getCreditCard(true)
    }

    @Unroll({"person $field is $error using $val"})
    def "test person birth date constraints"() {
        when:
        def obj = new Person("$field": val)

        then:
        validateConstraints(obj, field, error)

        where:
        error      | field       | val
        'max'      | 'birthDate' | new Date() + 1
        'nullable' | 'birthDate' | null
        'valid'    | 'birthDate' | new Date() - 1
        'valid'    | 'birthDate' | new Date()
    }

    @Unroll({"person $field testing $error"})
    def "test person children constraints"() {
        when:
        def obj = new Person("$field": val)

        then:
        validateConstraints(obj, field, error)

        where:
        error     | field      | val
        'maxSize' | 'children' | createPerson(11)
        'minSize' | 'children' | createPerson(1)
        'valid'   | 'children' | null
        'valid'   | 'children' | createPerson(10)
        'valid'   | 'children' | createPerson(2)
    }

    private createPerson(Integer count) {
        def persons = []
        count.times {
            persons << new Person()
        }
        persons
    }
}
