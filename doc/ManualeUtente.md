# ClimaScope: Manuale Utente
ClimaScope è un tool che permette a cittadini e ai centri di monitoraggio di seguire e aggiornare le condizioni climatiche dei propri luoghi di interesse. In particolare, operatori registrati e iscritti ad un centro di monitoraggio, possono inserire rilevazioni su una serie di parametri che misurano la situazione climatica delle aree di interesse seguite dal loro centro. I comuni cittadini possono quindi consultare il programma qualora volessero informarsi della situazione in un'area di loro interesse.

Questo manuale è organizzato in due sezioni principali: una sezione generica rivolta a tutti gli utenti e quella specifica per gli operatori registrati.

## Come avviare il programma
Il programma è stato testato su Linux e Windows e per lanciarlo è necessario Java SE 17. Non è assicurato che il programma funzioni in ambiente Mac OS. 

Per lanciare il programma sono disponibili due differenti opzioni, a seconda del sistema operativo installato sul proprio computer. In particolare, su Linux, doppo essersi spostati nella cartella del programma:

    $ cd path/to/ClimaScope
    $ sudo chmod +x ./ClimaScope.sh
    $ ./ClimaScope.sh
Su Windows, dopo essersi spostati nella cartella del programma, basta cliccare due volte sul file `ClimaScope.bat`. 


# Funzionalità per il cittadino
## Pagina principale
All'apertura, la seguente schermata verrà visualizzata: 
![title](Images/example.png)
Gli elementi nella finestra sono i seguenti:
- barra di ricerca per le aree di interesse, che supporta sia ricerca per nome che per coordinate. Le aree registrate sono tutte le città con una popolazione maggiore di 10.000 abitanti disponbili nella banca dati [GeoNames](http://download.geonames.org/export/zip/). 
- bottoni per il Login e la Registrazione per gli operatori.

### Ricerca per nome
Selezionando la barra di ricerca è possibile cercare il nome di un'area di interesse: una lista di suggerimenti verrà visualizzata e aggiornata in modo dinamico a seconda della stringa cercata.

La figura sotto riporta un esempio di ricerca per nome:
![title](Images/example.png)

### Ricerca per coordinate
Un altro tipo di ricerca possibile è quella per coordinate. Per attivare questa modalità è necessario immettere nel campo di ricerca una stringa che abbia il seguente formato:
```
LAT° LAT_SIGN LONG LONG_SIGN 
```
dove `LAT` e `LONG` sono due numeri *decimali* (con il punto come separatore tra parte intera e parte decimale), `LAT_SIGN` può assumere i valori **N** (per NORD) e **S** (per SUD) e `LONG_SIGN` può assumere i valori **E** (per EST) e **W** (per OVEST).

Come caso della ricerca per nome verrà visualizzata una lista di suggerimenti, in questo caso ordinata in ordine ascendente in termini di distanza dalle coordinate ricercate. 
**Nota bene**: tale lista viene visualizzata solo nel caso in cui esistano aree registrate nella banca dati di ClimaScope che siano a meno di 50 km dalle coordinate ricercate. Ad esempio, se la seguente stringa viene immessa nella barra di ricerca *45.46° N 9.18° E*, il primo suggerimento sarà Milano (che è la città più vicina a tali coordinate), e via via le altre aree registrate che stanno in un raggio di 50 km dal punto cercato. D'altro canto, cercando le seguenti coordinate *38.4° N 44.9° W*, la lista di suggerimenti non verrà visualizzata, in quanto si sta cercando un punto nel mezzo dell'Oceano Atlantico.

---
Un doppio click su un elemento della lista dei suggerimenti porterà l'utente alla pagina di dettaglio dell'area selezionata.

## Pagina di dettaglio
E' la pagina in cui l'utente può visualizzare la situazione climatica dell'area che ha selezionato. I parametri climatici rilevati più di recente vengono mostrati all'accesso nella pagina, insieme ad alcune informazioni riguardanti il centro di monitoraggio che ha effettuato la rilevazione  e ad eventuali note inserite al moomento della rilevazione (vedi figura).
![title](Images/example.png)

E' possibile ripercorrere la storia temporale delle rilevazioni usando il menù a tendina presente di fianco al nome dell'area selezionata (vedi figura).
![title](Images/example.png)

Nella tabella dei parametri climatici viene mostrata anche il valore medio delle rilevazioni nel tempo (vedi figura). 
![title](Images/example.png)

I parametri climatici indicano l'intensità del fenomeno che descrivono e sono suddivisi in fasce, da CRITICAL a EXCELLENT. Le categorie sono:
 - **Wind**: indica la velocità del vento in km/h;
 - **Humidity**: indica la % di umidità;
 - **Pressure**: indica la pressione in hPa;
 - **Temperature**: indica la temperatura in °C;
 - **Rainfall**: misura le precipitazioni in mm/h;
 - **Glaciers altitude**: misura l'altezza a cui si trovano i ghiacciai, in m;
 - **Glaciers mass**: misura la massa dei ghiacciai, in kg.

Le seguenti tabelle descrivono le fasce in maniera più dettagliata per ogni parametro:
### Vento
|Livello| Descrizione |
|--|--|
| EXCELLENT| 0 - 5 km/h, rischi nulli: la direzione del vento è indicata dalla deriva del fumo. Le bandiere non vengono mosse. Il vento muove appena le foglie degli alberi.  |
| FAVORABLE | 6 - 15 km/h, rischi molto bassi: il vento si sente sul viso. Le foglie frusciano e i rametti si muovono.  |
| MODERATE | 16 - 29 km/h, rischi medio-bassi: il vento solleva le foglie secche dal terreno. Le bandiere sono distese. Questa situazione inizia ad essere fastidiosa. |
| SEVERE | 30 - 55 km/h, rischi medio-alti: grandi rami in continuo movimento. Fischi percepiti nelle linee elettriche e telefoniche aeree o vicine. Ombrelli usati con difficoltà. Difficoltà a camminare controvento.|
| CRITICAL | Sopra i 56 km/h, rischi alti: rischi di piccoli danni strutturali, come tegole saltate e antenne televisive danneggiate. Sopra gli 85 km/h si verificano danni strutturali considerevoli, soprattutto sui tetti. Piccoli alberi possono essere travolti e sradicati.|

### Umidità
|Livello| Descrizione |
|--|--|
| EXCELLENT | 40% - 60%, a seconda della temperatura. Livello ottimale per la salute |
| FAVORABLE | 30% - 40%, 60% - 70%, a seconda della temperatura. Situazione mediamente ottimale. |
| MODERATE | 20% - 30%, 70% - 80%, a seconda della temperatura. Situazione di iniziale disagio. |
| SEVERE |  10% - 20%, 80% - 90%, a seconda della temperatura. Situazione di medio disagio.|
| CRITICAL | 0% - 10%, 90% - 100%, a seconda della temperatura. Situazione di forte disagio. |


### Pressione
|Livello| Descrizione |
|--|--|
| EXCELLENT | Pressione nella media per la stagione, nessun rischio per la salute. |
| FAVORABLE |  Livello sopportabile, possibile affaticamento durante l'attività fisica. |
| MODERATE | Pressione fuori dalla media di stagione, con rischi per le persone anziane e bambini. |
| SEVERE |  Situazione di disagio, pressione fuori dalla media stagionale, con possibili eventi atmosferici estremi. Ripercussioni medio-gravi sulla salute di anziani e bambini.  |
| CRITICAL | Situazione di estremo disagio, probabili eventi estremi con rischi gravi per la salute di tutti e le strutture.  |

### Temperatura
|Livello| Descrizione |
|--|--|
| EXCELLENT | Temperature nella media della stagione. Rischi per la salute nulli. |
| FAVORABLE | Temperatura sopportabile durante la stagione estiva, possibile affaticamento durante l'attività fisica. Durante la stagione invernale, temperatura sopportabile che non comporta rischi per la salute.|
| MODERATE | Temperature fuori dalla media di stagione, con rischi per le persone anziane e bambini. |
| SEVERE |  Situazione di disagio, temperature fuori dalla media stagionale, con picchi estremi. Ripercussioni medio-gravi sulla salute di anziani e bambini. |
| CRITICAL | Situazione di estremo disagio, ondate di caldo/freddo portano a rischi gravi per la salute di tutti. |


### Precipitazioni
|Livello| Descrizione |
|--|--|
| EXCELLENT | Pioggia nulla, precipitazioni < 0.5 mm/h. Nessun disagio. |
| FAVORABLE | Pioggia lieve, con precipitazioni 0.6 - 2.5 mm/h, nessun disagio, ombrello non strettamente necessario. |
| MODERATE | Pioggia normale, 2.6 - 7.5 mm/h, nessun disagio, ma ombrello necessario. |
| SEVERE |  Pioggia forte, 7.6 - 40 mm/h, possibili disagi in zone a rischio come allagamenti. |
| CRITICAL | Pioggia fortissima, > 41 mm/h, forti disagi nelle zone a rischio, possibili disagi nelle zone non a rischio. |

### Altezza dei ghiacciai
|Livello| Descrizione |
|--|--|
| EXCELLENT | Livello normale nella media della stagione. |
| FAVORABLE | Livello leggermente più alto dalla media stagionale, nessun rischio per la produzione energetica e per l'economia. |
| MODERATE | Livello più alto della media stagionale, possibili disagi per le riserve d'acqua che potrebbero coinvolgere le aziende e la produzione energetica nelle stagioni più calde. |
| SEVERE | Livello decisamente più alto della media stagionale, disagi per le riserve d'acqua, rischio di siccità con ripercussioni sulla produzione energetica durante la primavera e l'estate.  |
| CRITICAL | Livello estrememente più alto della media stagionale, siccità durante tutto l'anno. |

### Massa dei ghiacciai
|Livello| Descrizione |
|--|--|
| EXCELLENT | Livello normale nella media della stagione. |
| FAVORABLE | Livello leggermente più basso dalla media stagionale, nessun rischio per la produzione energetica e per l'economia. |
| MODERATE | Livello più basso della media stagionale, possibili disagi per le riserve d'acqua che potrebbero coinvolgere le aziende e la produzione energetica nelle stagioni più calde. |
| SEVERE | Livello decisamente più basso della media stagionale, disagi per le riserve d'acqua, rischio di siccità con ripercussioni sulla produzione energetica durante la primavera e l'estate.  |
| CRITICAL | Livello estrememente più basso della media stagionale, siccità durante tutto l'anno. |

# Funzionalità per l'operatore
## Pagina principale
Oltre alle funzionalità descritte per il cittadino, che valgono anche per gli operatori registrati, due funzionalità aggiuntive sono disponibili per questi ultimi: Login e Registrazione.

### Login
Permette all'utente registrato di accedere al tool (vedi figura).
![title](Images/example.png)

### Registrazione
Permette ad un operatore di registrarsi, dopo aver compilato il form di registrazione. Nel form l'operatore dovrà scegliere il centro di monitoraggio di appartenenza (vedi figura).
![title](Images/example.png)

Se il centro di monitoraggio non è ancora stato registrato, gli verrà data la possibilità di crearlo (vedi figura). Durante la creazione, dovranno essere aggiunte le aree di interesse relative al centro di monitoraggio, usando direttamente la barra di ricerca. In questo caso, al doppio click su un elemento della lista di suggerimenti, l'elemento verrà aggiunto alla lista delle aree di interesse del nuovo centro di monitoraggio (vedi figure).
![title](Images/example.png)  

## Pagina di dettaglio
Una volta che un operatore ha eseguito l'accesso, avrà la possibilità di registrare una nuova rilevazione nella pagina di dettaglio di ognuna delle aree monitorate dal centro di monitoraggio a cui appartiene. Dopo aver registrato i nuovi parametri e aver eventualmente inserito una nota, potrà cliccare il bottone **Save** per salvare la nuova rilevazione ed uscire dalla pagina di dettaglio (vedi figura).
![title](Images/example.png)

