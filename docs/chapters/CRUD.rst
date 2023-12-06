CRUD tests
==========

Create
------

* Sikeres létrehozás (például hallgatói regisztráció)
* Hiányzó adatok vagy hibás adatok (érvénytelen születési dátum, üres sztring mint név)
* Nincs jogosultság (például ZV létrehozásánál)
* Már létező név vagy egyéb ütközés (például már regisztrált Neptun kód vagy e-mail cím)

Read
----

* Sikeres lekérdezés
* Nincs jogosultság
* Érvénytelen azonosító (non existing ID)

Update
------

* Sikeres update
* Nincs jogosultság
* Érvénytelen ID
* Hiányzó, hibás adat
* Névütközés, egyéb adatintegritási hiba

Remove
------

* Sikeres törlés
* Nincs jogosultság
* Érvénytelen ID
* Kapcsolódó rekordok esetében integritási probléma jelzése
