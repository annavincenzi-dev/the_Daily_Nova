# The Daily Nova
## Sistema editoriale full-stack sviluppato in Java e Spring Boot.

## Indice
- [User Stories](#user-stories)
- [Stack](#stack)
- [Front-end](#front-end)
- [Gallery](#gallery)

# User Stories
Il progetto è stato sviluppato sulla base di cinque User Stories, che definiscono le funzionalità principali attese dall'applicazione. Ogni User Story rappresenta un'esigenza concreta di un eventuale utente e ha guidato le scelte progettuali e implementative.


## User Story #1
### Registrazione, Login e Creazione Articoli

- **Come Sara** voglio registrarmi in piattaforma per inserire un articolo, in modo tale da lavorare per The Daily Nova.

#### Acceptance Criteria
- Utente può registrarsi e loggarsi (Spring Security + form custom)
- Bottone "inserisci articolo" visibile solo se loggato
- Articolo con titolo, sottotitolo, corpo
- Categorie pre-compilate
- Relazione 1-a-N tra Utente -> Articolo
- Relazione 1-a-N tra Categoria -> Articolo
- Messaggio di conferma dopo l'inserimento dell'articolo.

## User Story 2
### Visualizzazione articoli, ricerca per categoria/autore e gestione immagini

- **Come Lorenzo** vorrei visualizzare gli ultimi articoli sul portale in modo tale da informarmi su ciò che succede nel mondo.

#### Acceptance Criteria 
- Manipolazione delle immagini con supabase
- Nella home, solo gli articoli più recenti (scegliete voi numero)
- In home e index, ordine dal più recente al più vecchio
- Pagina dettaglio per ogni articolo 
- Click per ricerca per categoria
- Click per ricerca per scrittore

## User Story 3
### Gestione dei ruoli Admin e Revisor

- **Come Marta** vorrei poter contare su una funzione di fact-checking in modo tale da poter controllare quali notizie pubblicare.

#### Acceptance Criteria 
- Tre nuovi ruoli: Admin, Revisor, Writer
- Un utente registrato richiede di entrare a far parte del team tramite un form di "lavora con noi"
- Creazione di una dashboard per il proprietario della piattaforma per poter gestire le richieste
- Permettere solo all'admin la modifica e cancellazione delle categorie
- Gli utenti revisori avranno una sezione a loro dedicata con tutti gli articoli da revisionare
- Bottone accetta articolo
- Bottone rifiuta articolo

## User Story 4
### Ricerca Full-text

- **Come Lorenzo** vorrei poter cercare tra gli articoli in modo tale da visualizzare subito quello che mi interessa.

#### Acceptance Criteria 
- Implementazione della ricerca full-text
- Ricerca per titolo
- Ricerca per sottotitolo
- Ricerca per categoria

## User Story 5
### Gestione ruolo Writer

- **Come Sara**  vorrei poter cancellare o modificare gli articoli che ho scritto in modo tale da consegnare sempre un lavoro d'alta qualità.

#### Acceptance Criteria 
- Creazione di una dashboard dedicata ai writer
- Permettere solo al writer specifico la modifica di un articolo
- Permettere solo al writer specifico la cancellazione di un articolo
- EXTRA: Se l'articolo viene modificato, deve ritornare in revisione


# Stack
Il progetto è stato sviluppato utilizzando le seguenti tecnologie e strumenti:

## Back-end
- **Spring Boot**, framework principale per l'applicazione.
    - **Spring MVC**, per la gestione delle richieste HTTP.
    - **Spring Security**, per l'autenticazione e l'autorizzazione.
    - **CrudRepository / JpaRepository**, per la gestione semplificata delle operazioni CRUD e l’accesso ai dati attraverso JPA.
    - **Tomcat**, server di esecuzione embedded.
    - **Maven**,gestione delle dipendenze e build del progetto.

## Database & Storage
- **MySQL**, sistema di gestione del database relazionale.
- **SQL**, utilizzato sia per query manuali sia in file `.sql` per l’inizializzazione e il popolamento delle tabelle.
- **TablePlus**, interfaccia grafica per la gestione e l’ispezione del database.
- **Supabase Storage**, utilizzato per la gestione e l'archiviazione delle **immagini**, fornendo un bucket di storage cloud con API sicure per l'upload e il recupero. 


# Front-end

## Stack Front-end
- **HTML & CSS**, per la struttura e lo stile delle pagine web.
- **Bootstrap**, framework CSS usato per il layout responsive e i componenti UI.
- **Bootstrap Icons**, icone vettoriali leggere per arricchire l'interfaccia.
- **Thymeleaf**, template engine server-side integrato con Spring Boot, utilizzato per la generazione dinamica delle pagine HTML.

## Styles

- **Font**: Cutive Mono
    - [Google Fonts](https://fonts.google.com/specimen/Cutive+Mono)
- **Palette**: <br>
![Color Palette](/src/main/resources/static/images/Frame%201.png)


# Gallery

## Homepage </br>
![Homepage](/src/main/resources/static/images/gallery/home.png)

![Homepage](/src/main/resources/static/images/gallery/home2.png)

## Login </br>
![Login Form](/src/main/resources/static/images/gallery/login.png)

## Job Application Form </br>
![Send Career Request](/src/main/resources/static/images/gallery/CareerRequestSend.png)

## Admin Dashboard </br>
![admin dashboard](/src/main/resources/static/images/gallery/adminDash.png)

## Career request detail </br>
![career request detail](/src/main/resources/static/images/gallery/careerRequestDetail.png)

## Create category </br>
![create category form](/src/main/resources/static/images/gallery/createCategory.png)

## Update category </br>
![update category form](/src/main/resources/static/images/gallery/updateCategory.png)

## Writer Dashboard </br>
![writer dashboard](/src/main/resources/static/images/gallery/writerDash.png)

## Create article </br>
![create article form](/src/main/resources/static/images/gallery/createArticle.png)

## Edit article </br>
![edit article form](/src/main/resources/static/images/gallery/articleEdit.png)

## Revisor Dashboard </br>
![revisor dashboard](/src/main/resources/static/images/gallery/revisorDash.png)

## Check article </br>
![check article page](/src/main/resources/static/images/gallery/articleCheck.png)


<!-- TODO

- frontend 
v searchbar
v all articles
v dettaglio articolo
v writer/revisor/admin dashboards
v lavora con noi e footer

V EDIT ARTICOLO!
ALERT - writer dashboard success -admin dashboard success
MESSAGGI DI ERRORE 
README V
manca gallery

--responsiveness-->
