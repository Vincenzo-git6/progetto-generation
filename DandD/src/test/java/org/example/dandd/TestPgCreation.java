package org.example.dandd;

import org.example.dandd.model.dao.ActionDao;
import org.example.dandd.model.dao.EquipmentDao;
import org.example.dandd.model.dao.MonsterDao;
import org.example.dandd.model.dao.PgPlayableDao;
import org.example.dandd.model.entities.Action;
import org.example.dandd.model.entities.Equipment;
import org.example.dandd.model.entities.Monster;
import org.example.dandd.model.entities.enums.ActionType;
import org.example.dandd.model.entities.enums.CharacterType;
import org.example.dandd.model.entities.enums.Danger;
import org.example.dandd.model.entities.enums.EquipmentType;
import org.example.dandd.model.entities.pg.PgPlayable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// import java.util.ArrayList; // Non usate, possono essere rimosse
import java.util.List;

@SpringBootTest
public class TestPgCreation
{
	@Autowired
	PgPlayableDao pDao;
	@Autowired
	EquipmentDao eDao;
	@Autowired
	MonsterDao mDao;
	@Autowired
	ActionDao aDao;

	@Test
	public void testDb()
	{
		// --- PG Cloud Constantine (Troubleshooter) ---
		PgPlayable cloudPg = new PgPlayable();
		cloudPg.setCharacterType(CharacterType.TROUBLESHOOTER);
		cloudPg.setName("The President");
		cloudPg.setDescription("Un cecchino infallibile, armato del suo fido fucile 4NGUL4R," +
				" è pronto a sterminare le orde di bug. Vive per il brivido di premere il grilletto," +
				" e ha già un proiettile con un nome inciso: Bug Abissale. Un colpo, un morto.");
		cloudPg.setHp(80);
		cloudPg.setAtk(30);
		cloudPg.setDef(15);
		cloudPg.setSpeed(10);
		cloudPg.setImageUrl("https://i.imgur.com/vvEEMgU.png"); // URL Troubleshooter
		pDao.save(cloudPg);

		Equipment cloudWeaponF  = new Equipment();
		cloudWeaponF.setEquipmentType(EquipmentType.WEAPON);
		cloudWeaponF.setName("4NGUL4R");
		cloudWeaponF.setDescription("The President effettua un commit chirurgico direttamente sulla realtà." +
				" Con git add --patch, seleziona la porzione di codice difettosa nel nemico e la sostituisce con puro danno." +
				" Nessun messaggio di commit, solo dolore localizzato.");
		cloudWeaponF.setPlayable(cloudPg);
		cloudWeaponF.setPlusDmg(15);
		eDao.save(cloudWeaponF);

		Action cloudBaseAtk = new Action();
		cloudBaseAtk.setNameAction("Binding Fatale");
		cloudBaseAtk.setDescriptionAction("The President estende un [(ngModel)] tra il mirino del suo 4NGUL4R e il cuore del nemico.\n" +
				" Ogni movimento, ogni intenzione," +
				" ogni impulso si sincronizza in tempo reale.\n" +
				"Premere il grilletto è solo una formalità: il danno è già stato scritto nel template.");
		cloudBaseAtk.setMaxNumTarget(1);
		cloudBaseAtk.setActionType(ActionType.BASE);
		cloudBaseAtk.setPrecision(100);
		cloudBaseAtk.setEntity(cloudPg);
		aDao.save(cloudBaseAtk);


		Action cloudHeavyAtk = new Action();
		cloudHeavyAtk.setNameAction("Refresh Manuale");
		cloudHeavyAtk.setDescriptionAction("The President forza un refresh manuale su una build instabile. Ma non è un aggiornamento: è una sentenza.\n" +
				"Il bersaglio, incapace di gestire il nuovo ciclo di vita, va in ExpressionChangedAfterItHasBeenCheckedError.\n" +
				"E quando l'app si rompe… qualcosa esplode.");
		cloudHeavyAtk.setMaxNumTarget(1);
		cloudHeavyAtk.setActionType(ActionType.HEAVY);
		cloudHeavyAtk.setPrecision(75);
		cloudHeavyAtk.setEntity(cloudPg);
		aDao.save(cloudHeavyAtk);


		Action cloudSpecialAtk = new Action();  //danno 58.5
		cloudSpecialAtk.setNameAction("Iniezione Letale");
		cloudSpecialAtk.setDescriptionAction("The President forza una dependency injection nel cuore del nemico, iniettando un servizio instabile e privo di controlli.\n" +
				"Il bersaglio non riesce a risolvere il provider e collassa in un errore esistenziale.\n" +
				"Il modulo si autodistrugge. Stack trace: sconosciuto.");
		cloudSpecialAtk.setMaxNumTarget(2);
		cloudSpecialAtk.setActionType(ActionType.SPECIALE);
		cloudSpecialAtk.setPrecision(100);
		cloudSpecialAtk.setCooldown(2);
		cloudSpecialAtk.setMolt(1.3);
		cloudSpecialAtk.setEntity(cloudPg);
		aDao.save(cloudSpecialAtk);


		// --- PG V Alphamale (CodeThief) ---
		PgPlayable vPg = new PgPlayable();
		vPg.setCharacterType(CharacterType.CODETHIEF);
		vPg.setName("V Alphamale");
		vPg.setDescription("Un'ombra nel codice, era lì un minuto fa, non hai refreshato la pagina, dov'è finito? Molto semplice, dietro di te. " +
				" Il Code Thief è un abile assassino di bug, una presenza invisibile che colpisce con precisione chirurgica e quando meno te l'aspetti." +
				" Armato dei suoi due pugnali INT e DOUBLE, attacca dall'ombra, approfittando della minima distrazione dei bug per ripulire il codice.");
		vPg.setHp(120);
		vPg.setAtk(25);
		vPg.setDef(15);
		vPg.setSpeed(40);
		vPg.setImageUrl("https://i.imgur.com/VMk9hOb.png"); // URL CodeThief
		pDao.save(vPg);

		Equipment vWeaponF  = new Equipment();
		vWeaponF.setEquipmentType(EquipmentType.WEAPON);
		vWeaponF.setName("CTRL-C");
		vWeaponF.setDescription("Affilato come un comando in shell, questo pugnale ruba più di righe di codice: sottrae l’essenza stessa del nemico.\n" +
				"Un colpo ben assestato, e la sua identità viene copiata — ogni pattern, ogni bug, ogni paura.\n" +
				"Il cursore lampeggia. È già troppo tardi.”");
		vWeaponF.setPlayable(vPg);
		vWeaponF.setPlusDmg(15);
		eDao.save(vWeaponF);

		Equipment vWeaponS  = new Equipment();
		vWeaponS.setEquipmentType(EquipmentType.WEAPON);
		vWeaponS.setName("CTRL-V");
		vWeaponS.setDescription("Il gemello oscuro di CTRL-C. Una volta copiato il bersaglio, questo pugnale ne incolla la condanna.\n" +
				"Ma non nel codice… nel dolore.\n" +
				"CTRL-V incide la sentenza, riga dopo riga, e la scrive direttamente nella RAM dell’inferno.");
		vWeaponS.setPlayable(vPg);
		vWeaponS.setPlusDmg(15);
		eDao.save(vWeaponS);

		Action vBaseAtk = new Action();
		vBaseAtk.setNameAction("Bug Slash");
		vBaseAtk.setDescriptionAction("V appare e scompare come una riga di codice che sfugge alla compilazione.\n" +
				" Con un movimento fulmineo, brandisce CTRL-C per eliminare il bug da ogni angolo.\nNon c’è traccia della sua azione, " +
				"solo il risultato: il nemico è rimosso dal codice in un istante.\nNessuna dichiarazione di morte, nessuna eccezione sollevata.");
		vBaseAtk.setMaxNumTarget(1);
		vBaseAtk.setActionType(ActionType.BASE);
		vBaseAtk.setPrecision(100);
		vBaseAtk.setEntity(vPg);
		aDao.save(vBaseAtk);

		Action vHeavyAtk = new Action();
		vHeavyAtk.setNameAction("Hotfix Invisibile");
		vHeavyAtk.setDescriptionAction("V non lascia tracce: le sue lame attraversano il codice come una patch segreta, invisibile agli occhi." +
				"\nColpisce in un angolo morto, dove l’errore non può più essere tracciato." +
				"\nL'errore viene sistemato... ma solo temporaneamente.");
		vHeavyAtk.setMaxNumTarget(1);
		vHeavyAtk.setActionType(ActionType.HEAVY);
		vHeavyAtk.setPrecision(70);
		vHeavyAtk.setEntity(vPg);
		aDao.save(vHeavyAtk);

		Action vSpecialAtk = new Action();  //danno 71.5
		vSpecialAtk.setNameAction("CTRL-X");
		vSpecialAtk.setDescriptionAction("La mossa finale. Un colpo devastante che rimuove definitivamente ogni errore, ma non senza conseguenze." +
				"\nV Alphamale attiva il comando CTRL-X e cancella l'intero modulo nemico dalla memoria.\n" +
				"Il codice è pulito, ma la loro esistenza è ormai solo un log remoto.");
		vSpecialAtk.setMaxNumTarget(1);
		vSpecialAtk.setActionType(ActionType.SPECIALE);
		vSpecialAtk.setPrecision(100);
		vSpecialAtk.setMolt(1.3);
		vSpecialAtk.setCooldown(2);
		vSpecialAtk.setEntity(vPg);
		aDao.save(vSpecialAtk);

		// --- PG Heimdall (Infrastructure) ---
		PgPlayable heimdallPg = new PgPlayable();
		heimdallPg.setCharacterType(CharacterType.INFRASTRUCTURE);
		heimdallPg.setName("Heimdall");
		heimdallPg.setDescription("Il guardiano dei Vendicatori, un muro invalicabile. Equipaggiato con il suo enorme scudo PROTECTED, " +
				"è il muro contro cui s'infrange l'oscurità, difensore del gruppo.\n" +
				"Se i bug vogliono raggiungere i suoi compagni, avranno bisogno di molta, molta fortuna. Di qui non si passa.");
		heimdallPg.setHp(160);
		heimdallPg.setAtk(15);
		heimdallPg.setDef(25);
		heimdallPg.setSpeed(10);
		heimdallPg.setImageUrl("https://i.imgur.com/qljpjPH.png"); // URL Heimdall
		pDao.save(heimdallPg);

		Equipment heimdallWeaponF  = new Equipment();
		heimdallWeaponF.setEquipmentType(EquipmentType.SHIELD);
		heimdallWeaponF.setName("EGIDA DI STEFANO");
		heimdallWeaponF.setDescription("L'Egida di Stefano non è solo uno scudo, è un legame sacro che respinge ogni minaccia." +
				"\nCreato per difendere i più vulnerabili, questo scudo resiste all'impossibile.\n" +
				"Ogni colpo che lo colpisce si infrange come un'eccezione non gestita, lasciando il nemico senza via di fuga. " +
				"\nL'oscurità si schianta contro questa muraglia, incapace di oltrepassarla.");
		heimdallWeaponF.setPlayable(heimdallPg);
		heimdallWeaponF.setPlusDef(5);
		eDao.save(heimdallWeaponF);


		Equipment heimdallWeaponS  = new Equipment();
		heimdallWeaponS.setEquipmentType(EquipmentType.WEAPON);
		heimdallWeaponS.setName("NULL POINTER KNUCKLES");
		heimdallWeaponS.setDescription("DA FARE");
		heimdallWeaponS.setPlayable(heimdallPg);
		heimdallWeaponS.setPlusDmg(10);
		eDao.save(heimdallWeaponS);

		Action heimdallBaseAtk = new Action();
		heimdallBaseAtk.setNameAction("Pugno del Commit");
		heimdallBaseAtk.setDescriptionAction("Heimdall lancia un colpo tanto preciso quanto definitivo, come un commit che salva definitivamente lo stato del codice.\n" +
				"Il bug non ha possibilità di sfuggire, poiché ogni movimento viene bloccato da un controllo seherPg." +
				"\nÈ un attacco che applica un rollback istantaneo all'errore, eliminandolo dalla memoria e riportando il codice al suo stato originario, privo di difetti.");
		heimdallBaseAtk.setMaxNumTarget(1);
		heimdallBaseAtk.setActionType(ActionType.BASE);
		heimdallBaseAtk.setPrecision(100);
		heimdallBaseAtk.setEntity(heimdallPg);
		aDao.save(heimdallBaseAtk);

		Action heimdallHeavyAtk = new Action();
		heimdallHeavyAtk.setNameAction("Refactor del Muro");
		heimdallHeavyAtk.setDescriptionAction("Heimdall non si accontenta di fermare i bug, li ristruttura nel loro stato di caos. Con il suo potere, effettua un refactor totale del codice avversario, smontando ogni linea di errore, eliminando i nodi difettosi e ripristinando il sistema ad uno stato perfetto.\n" +
				"Il bug subisce un ciclo di vita che si interrompe in una build fallita, incapace di rimanere nel sistema." +
				"\nUn attacco che ripulisce e ripristina.");
		heimdallHeavyAtk.setMaxNumTarget(1);
		heimdallHeavyAtk.setActionType(ActionType.HEAVY);
		heimdallHeavyAtk.setPrecision(90);
		heimdallHeavyAtk.setEntity(heimdallPg);
		aDao.save(heimdallHeavyAtk);

		Action heimdallSpecialAtk = new Action();
		heimdallSpecialAtk.setNameAction("Purge del Codice");
		heimdallSpecialAtk.setDescriptionAction("Heimdall invoca il purge definitivo, dove ogni bit di codice difettoso viene eliminato in un colpo solo. L’errore è cancellato completamente dalla memoria, proprio come se fosse stato rimosso da Git.\n" +
				"In questo attacco, il bug non ha via di scampo: viene rimosso dal codice e il sistema si ripristina, perfetto come il primo commit." +
				"\nUn attacco che ripulisce e ripristina.");
		heimdallSpecialAtk.setMaxNumTarget(1);
		heimdallSpecialAtk.setActionType(ActionType.SPECIALE);
		heimdallSpecialAtk.setCooldown(1);
		heimdallSpecialAtk.setMolt(1.5);
		heimdallSpecialAtk.setPrecision(100);
		heimdallSpecialAtk.setEntity(heimdallPg);
		aDao.save(heimdallSpecialAtk);

		// --- PG Skibidi Sanni (DataMystic) ---
		PgPlayable sanniPg = new PgPlayable();
		sanniPg.setCharacterType(CharacterType.DATAMYSTIC);
		sanniPg.setName("The San");
		sanniPg.setDescription("Una misteriosa figura incappucciata, il volto mai visibile. " +
				"Nessuno sa chi sia, possiede solo la sua tunica e il suo leggendario Grimorio,  REPOSITORY." +
				" Lanciando palle di fuoco digitali, stringhe di codice e beans da spring, " +
				"devasta il campo da battaglia con i suoi attacchi ad area distruttivi. Il Bug Abissale non ha speranze.");
		sanniPg.setHp(80);
		sanniPg.setAtk(20);
		sanniPg.setDef(12);
		sanniPg.setSpeed(20);
		sanniPg.setImageUrl("https://i.imgur.com/zMoFpRa.png"); // URL SkibidiSanni
		pDao.save(sanniPg);

		Equipment sanniWeaponF  = new Equipment();
		sanniWeaponF.setEquipmentType(EquipmentType.WEAPON);
		sanniWeaponF.setName("SFERA DI DATI");
		sanniWeaponF.setDescription("Questa misteriosa sfera, avvolta in un alone di energia digitale, amplifica e condensa il potere di ogni bit e byte." +
				"\nQuando impugnata, sembra pulsare con la forza dell'informazione stessa, pronta a liberare l'energia dei dati su ogni nemico.\n" +
				"Ogni colpo inferto dalla Sfera di Dati è un flusso di potenza incontrollabile che penetra nel cuore del sistema, causando danni devastanti e interferenze al codice nemico.");
		sanniWeaponF.setPlayable(sanniPg);
		sanniWeaponF.setPlusDmg(10);
		eDao.save(sanniWeaponF);

		Action sanniBaseAtk = new Action();
		sanniBaseAtk.setNameAction("Data Bolt");
		sanniBaseAtk.setDescriptionAction("Con un gesto rapido, The San lancia un proiettile di dati energetici.\n" +
				"Carico di energia pura e diretta, il Data Bolt attraversa il campo di battaglia con una velocità impressionante, colpendo il bersaglio con la precisione di un algoritmo ottimizzato.\n" +
				"Ogni impatto è come una query SQL che restituisce un errore fatale: il nemico viene travolto dalla forza dei dati, subendo danni devastanti in un istante.");
		sanniBaseAtk.setMaxNumTarget(1);
		sanniBaseAtk.setActionType(ActionType.BASE);
		sanniBaseAtk.setPrecision(100);
		sanniBaseAtk.setEntity(sanniPg);
		aDao.save(sanniBaseAtk);

		Action sanniHeavyAtk = new Action();
		sanniHeavyAtk.setNameAction("Data Tempest");
		sanniHeavyAtk.setDescriptionAction("Una tempesta di dati energetici scatena il caos sul campo da battaglia.\n" +
				"The San lancia un Data Surge che esplode con un'energia inarrestabile, annientando tutto ciò che incontra.\n" +
				"I nemici vengono travolti da un flusso di informazioni così denso e impetuoso da corrompere i loro sistemi.\n" +
				"Non solo subiscono danni devastanti, ma il loro codice viene distrutto, come se fosse inghiottito da un black hole di dati. Nessuna difesa può resistere a questa furia informatica.");
		sanniHeavyAtk.setMaxNumTarget(1);
		sanniHeavyAtk.setActionType(ActionType.HEAVY);
		sanniHeavyAtk.setPrecision(80);
		sanniHeavyAtk.setEntity(sanniPg);
		aDao.save(sanniHeavyAtk);

		Action sanniSpecialAtk = new Action();
		sanniSpecialAtk.setNameAction("Quantum Debug");
		sanniSpecialAtk.setDescriptionAction("Con un movimento rapido, The San attiva il Quantum Debug: una tecnica che sfrutta il potere dei bit quantistici per annullare l'esistenza stessa di un bug.\n" +
				"Un colpo che non solo rimuove il nemico, ma riscrive la sua realtà in un istante.\n" +
				"È come se il codice nemico venisse resettato a un punto perfetto nel passato, annullando ogni traccia dell'errore.\n" +
				"Il bug non esiste più: è stato debuggato dalla storia stessa, come se non fosse mai esistito.");
		sanniSpecialAtk.setMaxNumTarget(3);
		sanniSpecialAtk.setActionType(ActionType.SPECIALE);
		sanniSpecialAtk.setCooldown(3);
		sanniSpecialAtk.setMolt(2);
		sanniSpecialAtk.setPrecision(100);
		sanniSpecialAtk.setEntity(sanniPg);
		aDao.save(sanniSpecialAtk);

		// --- PG Debug Her (GitBard) ---
		PgPlayable herPg = new PgPlayable();
		herPg.setCharacterType(CharacterType.GITBARD);
		herPg.setName("Debug Her");
		herPg.setDescription("Mai visto dei bug corrotti ballare distrattamente mentre vengono decimati? " +
				"Ora li hai visti. La GitBard, con il suono soave del suo herWeaponF ANNOTATION riesce a controllare la mente dei nemici per breve tempo," +
				" dominando il campo di battaglia e consentendo ai suoi compagni di fare piazza pulita.");
		herPg.setHp(130);
		herPg.setAtk(15);
		herPg.setDef(15);
		herPg.setSpeed(20);
		herPg.setImageUrl("https://i.imgur.com/qbVzjEL.png"); // URL DebugHer
		pDao.save(herPg);

		Equipment herWeaponF  = new Equipment();
		herWeaponF.setEquipmentType(EquipmentType.WEAPON);
		herWeaponF.setName("ANNOTATION");
		herWeaponF.setDescription("Un flauto incantato, la cui melodia affascina e ordina il caos.\n" +
				"Annotation è lo strumento che Her utilizza per comporre una sinfonia di correzioni, dove ogni nota suonata è un incantesimo che manipola il flusso del codice e dei bug.\n" +
				"Con un soffio, il flauto emette onde sonore che armonizzano il disordine e catalizzano la rimozione degli errori.\n" +
				"Ogni vibrazione, ogni melodia, è un comando che ordina il caos a piegarsi alla volontà del bardo, trasformando ogni bug in una semplice nota di una canzone perfetta.");
		herWeaponF.setPlayable(herPg);
		herWeaponF.setPlusDmg(10);
		eDao.save(herWeaponF);

		Action herBaseAtk = new Action();
		herBaseAtk.setNameAction("Patch Rapida");
		herBaseAtk.setDescriptionAction("Debug Her suona una rapida sequenza di note con il flauto, applicando una patch rapida al nemico.\n" +
				"Ogni nota è una correzione istantanea che risolve un piccolo errore nel codice nemico, infliggendo danni leggeri mentre rimuove imperfezioni temporanee.\n" +
				"Un attacco preciso che sfrutta la sua abilità nel correggere bug e anomalie nel flusso del sistema.");
		herBaseAtk.setMaxNumTarget(1);
		herBaseAtk.setActionType(ActionType.BASE);
		herBaseAtk.setPrecision(100);
		herBaseAtk.setEntity(herPg);
		aDao.save(herBaseAtk);

		Action herHeavyAtk = new Action();
		herHeavyAtk.setNameAction("Crash Sistemi");
		herHeavyAtk.setDescriptionAction("Debug Her lancia un attacco potente con il flauto, scatenando un'onda sonora che provoca un crash nel sistema nemico.\n" +
				"Le vibrazioni del flauto creano distorse nel codice, mandando in tilt il nemico con errori irreversibili.\n" +
				"Ogni suono causa stack overflow e eccezioni non gestite, infliggendo danni devastanti e causando il collasso del sistema nemico.");
		herHeavyAtk.setMaxNumTarget(1);
		herHeavyAtk.setActionType(ActionType.HEAVY);
		herHeavyAtk.setPrecision(100);
		herHeavyAtk.setEntity(herPg);
		aDao.save(herHeavyAtk);

		Action herHSpecialAtk = new Action();
		herHSpecialAtk.setNameAction("Refactor Armonico");
		herHSpecialAtk.setDescriptionAction("Debug Her intona un flusso melodico che refattorizza il codice vitale del gruppo.\n" +
				"Le note si diffondono nel sistema come pull request perfettamente documentate, eliminando ridondanze, correggendo bug e ottimizzando la salute di un alleato.\n" +
				"Un’armonia che rende il team più pulito, più stabile, e pronto per il prossimo deploy.");
		herHSpecialAtk.setMaxNumTarget(1);
		herHSpecialAtk.setActionType(ActionType.SPECIALE);
		herHSpecialAtk.setCooldown(2);
		herHSpecialAtk.setPrecision(100);
		herHSpecialAtk.setEntity(herPg);
		herHSpecialAtk.setTargetsAllies(true);
		herHSpecialAtk.setTargetsSelf(false);
		aDao.save(herHSpecialAtk);

		// --- PG Sandor Monster (CodeCleaner) ---
		PgPlayable sandorPg = new PgPlayable();
		sandorPg.setCharacterType(CharacterType.CODECLEANER);
		sandorPg.setName("Sandor Monster");
		sandorPg.setDescription("Il leggendario Decimatore di Bug, si dice che abbia camminato nell'abisso del codice Zoom e ne sia uscito indenne, " +
				"impugnando solo il suo spadone REFACTOR. Con solo ampi fendenti, e il suo giuramento di eliminare ogni sviluppatore Zoom dalla rete," +
				" è pronto ad affrontare le armate del Bug Abissale.");
		sandorPg.setHp(120);
		sandorPg.setAtk(25);
		sandorPg.setDef(17);
		sandorPg.setSpeed(20);
		sandorPg.setImageUrl("https://i.imgur.com/gCrgp9N.png"); // URL CodeCleaner
		pDao.save(sandorPg);

		Equipment sandorWeaponF  = new Equipment();
		sandorWeaponF.setEquipmentType(EquipmentType.WEAPON);
		sandorWeaponF.setName("REFACTOR");
		sandorWeaponF.setDescription("Uno strumento devastante travestito da buona pratica: quando Sandor impugna REFACTOR, il codice nemico viene smontato a colpi di rinomina e spostamenti insensati.\n" +
				"Nessun pattern è salvo, nemmeno il suo.");
		sandorWeaponF.setPlayable(sandorPg);
		sandorWeaponF.setPlusDmg(10);
		eDao.save(sandorWeaponF);

		Action sandorBaseAtk = new Action();
		sandorBaseAtk.setNameAction("Taurina Iniziale");
		sandorBaseAtk.setDescriptionAction("Sandor apre la prima Monster bianca del giorno con una scrollata e colpisce " +
				"al volo il nemico con una riga di codice confusa ma incredibilmente violenta.\n" +
				"È solo l'inizio, ma la taurina ha già fatto effetto.");
		sandorBaseAtk.setMaxNumTarget(1);
		sandorBaseAtk.setActionType(ActionType.BASE);
		sandorBaseAtk.setPrecision(100);
		sandorBaseAtk.setEntity(sandorPg);
		aDao.save(sandorBaseAtk);

		Action sandorHeavyAtk = new Action();
		sandorHeavyAtk.setNameAction("Refactor Senza Backup");
		sandorHeavyAtk.setDescriptionAction("Sotto l’effetto combinato di due Monster bianche e zero ore di sonno, " +
				"Sandor entra in un tunnel mentale e applica un refactor distruttivo senza Git né Dio.\n" +
				"Il codice nemico collassa in un commit che non si può pushare.");
		sandorHeavyAtk.setMaxNumTarget(1);
		sandorHeavyAtk.setActionType(ActionType.HEAVY);
		sandorHeavyAtk.setPrecision(80);
		sandorHeavyAtk.setEntity(sandorPg);
		aDao.save(sandorHeavyAtk);

		Action sandorSpecialAtk = new Action();
		sandorSpecialAtk.setNameAction("Sessione All-Nighter");
		sandorSpecialAtk.setDescriptionAction("Con tre Monster in corpo e lo sguardo perso nel terminale, " +
				"Sandor entra in trance.\n" +
				"Scaraventa sulla tastiera un dump di energia bruta e taurine-driven, cancellando tutto il buffer del nemico con un attacco che sa di delirio e vendetta.");
		sandorSpecialAtk.setMaxNumTarget(1);
		sandorSpecialAtk.setActionType(ActionType.SPECIALE);
		sandorSpecialAtk.setPrecision(50); // Precisione molto bassa, come da descrizione implicita
		sandorSpecialAtk.setMolt(3); // Moltiplicatore alto per compensare la bassa precisione
		sandorSpecialAtk.setCooldown(4); // Cooldown lungo
		sandorSpecialAtk.setEntity(sandorPg);
		aDao.save(sandorSpecialAtk);

		//Stefano
		PgPlayable stefano = new PgPlayable();
		stefano.setCharacterType(CharacterType.OMNICODER);
		stefano.setName("Professor Stefano Kernel");
		stefano.setDescription("Nel mondo oscuro e brutale del Codex Dungeon," +
				" dove i bug divorano la memoria e i runtime si spezzano come ossa" +
				", si erge una figura leggendaria: il Professor Stefano Kernel," +
				" il più temuto e rispettato tra gli Ingegneri del Codice. Al suo fianco c’è Sissyintax," +
				" una maltesina cibernetica addestrata a stordire virus grazie al suo abbaio amplificato." +
				" Ex maestro dell'Accademia di Generational Code, è rinomato per il suo stile inconfondibile:" +
				" cuffie sempre accese che captano le frequenze nascoste dell'infrastruttura corrotta e una camicia floreale intrisa di byte antichi," +
				" simbolo delle sue infinite ore trascorse a ottimizzare codici ed a rispondere a dubbi, domande o curiosità.");
		stefano.setHp(300);
		stefano.setAtk(80);
		stefano.setDef(50);
		stefano.setSpeed(70);
		stefano.setImageUrl("https://i.imgur.com/RW7nujI.png");
		pDao.save(stefano);

		Equipment stefanoWeaponF  = new Equipment();
		stefanoWeaponF.setEquipmentType(EquipmentType.WEAPON);
		stefanoWeaponF.setName("VETTORE");
		stefanoWeaponF.setDescription("Forgiata nel cyberspazio, taglia il caos del codice con precisione chirurgica. La sua energia, alimentata dal flusso del codice, emette un suono simile a un beep ogni volta che viene estratta, come se stesse compilando un nuovo programma.Si narra che il suo nome provenga da un errore di pronuncia di \"Array\".");
		stefanoWeaponF.setPlayable(stefano);
		stefanoWeaponF.setPlusDmg(69);
		eDao.save(stefanoWeaponF);

		Action stefanoBaseAtk = new Action();
		stefanoBaseAtk.setNameAction("Byte");
		stefanoBaseAtk.setDescriptionAction("Sissyntax da un morso al nemico lasciando il codice scoperto.");
		stefanoBaseAtk.setMaxNumTarget(1);
		stefanoBaseAtk.setActionType(ActionType.BASE);
		stefanoBaseAtk.setPrecision(100);
		stefanoBaseAtk.setEntity(stefano);
		aDao.save(stefanoBaseAtk);


		Action stefanoHeavyAtk = new Action();
		stefanoHeavyAtk.setNameAction("Patch Istantanea");
		stefanoHeavyAtk.setDescriptionAction("Cura se stesso e gli alleati correggendo bug critici nel sistema.");
		stefanoHeavyAtk.setMaxNumTarget(3);
		stefanoHeavyAtk.setTargetsSelf(false);
		stefanoHeavyAtk.setTargetsAllies(true);
		stefanoHeavyAtk.setActionType(ActionType.HEAVY);
		stefanoHeavyAtk.setPrecision(99);
		stefanoHeavyAtk.setEntity(stefano);
		aDao.save(stefanoHeavyAtk);


		Action stefanoSpecialAtk = new Action();
		stefanoSpecialAtk.setNameAction("Correzione \"carattere per carattere\"");
		stefanoSpecialAtk.setDescriptionAction("L'attacco più temuto da glitch ed errori nel codice. Stefano concentra tutta la sua energia in un devastante colpo mentale che ha dalla sua anni di odio, rancore e passione per il suo lavoro.");
		stefanoSpecialAtk.setMaxNumTarget(3);
		stefanoSpecialAtk.setActionType(ActionType.SPECIALE);
		stefanoSpecialAtk.setPrecision(99);
		stefanoSpecialAtk.setCooldown(2);
		stefanoSpecialAtk.setMolt(2);
		stefanoSpecialAtk.setEntity(stefano);
		aDao.save(stefanoSpecialAtk);


		// --- Mostro Javlin ---
		Monster javlin  = new Monster();
		javlin.setName("Javlin");
		javlin.setDescription("Un essere infimo e schifoso fatto di codici mai finiti ed errori mai letti carattere per carattere.");
		javlin.setHp(80);
		javlin.setAtk(20);
		javlin.setDef(5);
		javlin.setSpeed(5);
		javlin.setDanger(Danger.UNCHECKED);
		javlin.setImageUrl("https://i.imgur.com/d0NfQhC.png"); // URL Javlin
		mDao.save(javlin);

		Action graffioBase = new Action();
		graffioBase.setNameAction("Graffio");
		graffioBase.setDescriptionAction("Un graffio superficiale ma fastidioso, come un typo non rilevato dal linter.");
		graffioBase.setMaxNumTarget(1);
		graffioBase.setActionType(ActionType.BASE);
		graffioBase.setPrecision(100);
		graffioBase.setEntity(javlin); // Usa l'oggetto javlin appena creato
		aDao.save(graffioBase);

		Action pushMaligno = new Action();
		pushMaligno.setNameAction("Push Maligno");
		pushMaligno.setDescriptionAction("Esegue un push su main alle 18:01 di venerdì. La build esplode e il bug ride.");
		pushMaligno.setMaxNumTarget(2);
		pushMaligno.setActionType(ActionType.SPECIALE);
		pushMaligno.setPrecision(80);
		pushMaligno.setMolt(1.2); // Aggiunto moltiplicatore
		pushMaligno.setCooldown(2); // Aggiunto cooldown
		pushMaligno.setEntity(javlin);
		aDao.save(pushMaligno);

		// --- Mostro Spring-Ghoul ---
		Monster springGoul  = new Monster();
		springGoul.setName("Spring-Ghoul");
		springGoul.setDescription("Una creatura cibernetica con un corpo metallico, corroso e pieno di circuiti a vista. I suoi occhi brillano di un rosso intenso, e dalle sue mani artigliate si sprigionano scintille elettriche. Indossa dei resti di vestiti strappati, che rivelano i suoi innesti meccanici.");
		springGoul.setHp(80);
		springGoul.setAtk(20);
		springGoul.setDef(5);
		springGoul.setSpeed(5);
		springGoul.setDanger(Danger.UNCHECKED);
		springGoul.setImageUrl("https://i.imgur.com/G3gbNgi.png"); // URL springGhoul
		mDao.save(springGoul);

		Action morsoBase = new Action();
		morsoBase.setNameAction("Morso");
		morsoBase.setDescriptionAction("Un morso superficiale ma fastidioso, come un typo non rilevato dal linter.");
		morsoBase.setMaxNumTarget(1);
		morsoBase.setActionType(ActionType.BASE);
		morsoBase.setPrecision(100);
		morsoBase.setEntity(springGoul);
		aDao.save(morsoBase);

		Action spintaMaligna = new Action();
		spintaMaligna.setNameAction("Spinta Maligna");
		spintaMaligna.setDescriptionAction("Esegue un push su main alle 18:01 di venerdì. La build esplode e il bug ride.");
		spintaMaligna.setMaxNumTarget(2);
		spintaMaligna.setActionType(ActionType.SPECIALE);
		spintaMaligna.setPrecision(80);
		spintaMaligna.setMolt(1.2); // Aggiunto moltiplicatore
		spintaMaligna.setCooldown(2); // Aggiunto cooldown
		spintaMaligna.setEntity(springGoul);
		aDao.save(spintaMaligna);


		// --- Mostro Angulorc ---
		Monster angulorc = new Monster();
		angulorc.setName("Angulorc");
		angulorc.setDescription("Orchi guerrieri di Angular, attaccano usando delle String come clave, molto forti fisicamente ma poco intelligenti");
		angulorc.setDanger(Danger.CHECKED);
		angulorc.setHp(140);
		angulorc.setAtk(25);
		angulorc.setDef(12);
		angulorc.setSpeed(25);
		angulorc.setImageUrl("https://i.imgur.com/j0Dj5gz.png"); // URL Angul0rc
		mDao.save(angulorc);

		Action baseAngulorc= new Action();
		baseAngulorc.setActionType(ActionType.BASE);
		baseAngulorc.setNameAction("Pugno di stringhe");
		baseAngulorc.setMaxNumTarget(1);
		baseAngulorc.setDescriptionAction("Un pugno grezzo, potente come un `any` usato senza motivo.");
		baseAngulorc.setPrecision(100);
		baseAngulorc.setEntity(angulorc);
		aDao.save(baseAngulorc);

		Action specialAngulorc= new Action();
		specialAngulorc.setActionType(ActionType.SPECIALE);
		specialAngulorc.setMolt(1.4);
		specialAngulorc.setCooldown(2);
		specialAngulorc.setNameAction("ByteSlam"); // Nome azione corretto
		specialAngulorc.setMaxNumTarget(1);
		specialAngulorc.setDescriptionAction("L'Angulorc sbatte la sua clava di stringhe con la forza bruta di un ciclo infinito, causando danni significativi.");
		specialAngulorc.setPrecision(90); // Leggermente meno preciso
		specialAngulorc.setEntity(angulorc);
		aDao.save(specialAngulorc); // Salva l'azione speciale, non quella base di nuovo

		// --- Mostro OgreSql ---
		Monster ogreSql = new Monster();
		ogreSql.setName("OgreSQL"); // Nome corretto
		ogreSql.setDanger(Danger.CHECKED);
		ogreSql.setDescription("Grossi ogre corazzati messi a guardia dei Database SQL, sono ben protetti da armature di Query, attaccano con i loro pugni");
		ogreSql.setHp(160);
		ogreSql.setAtk(20);
		ogreSql.setDef(18);
		ogreSql.setSpeed(10);
		ogreSql.setImageUrl("https://i.imgur.com/ashkKXU.png"); // URL OgreSQL
		mDao.save(ogreSql);

		Action baseOgre= new Action();
		baseOgre.setActionType(ActionType.BASE);
		baseOgre.setNameAction("Pugno Corazzato");
		baseOgre.setMaxNumTarget(1);
		baseOgre.setDescriptionAction("Un pugno lento ma pesante, protetto da strati di query complesse.");
		baseOgre.setPrecision(100);
		baseOgre.setEntity(ogreSql);
		aDao.save(baseOgre);

		Action specialeOgre= new Action();
		specialeOgre.setActionType(ActionType.SPECIALE);
		specialeOgre.setMolt(1.5);
		specialeOgre.setCooldown(3);
		specialeOgre.setNameAction("QuerySmash"); // Nome azione corretto
		specialeOgre.setMaxNumTarget(3); // Colpisce più bersagli? Modificato da 1 a 3
		specialeOgre.setDescriptionAction("L'Ogre sferra un colpo devastante che rallenta i processi nemici come una query mal ottimizzata."); // Aggiunto effetto rallentamento (solo descrittivo qui)
		specialeOgre.setPrecision(85);
		specialeOgre.setEntity(ogreSql);
		aDao.save(specialeOgre);


		// --- Mostro Back-Ender ---
		Monster backEnder = new Monster();
		backEnder.setName("Back-Ender");
		backEnder.setDescription("Maestosi e misteriosi cavalieri neri, i terminatori del back end, formidabili distruttori di codice. Si vocifera che uno solo di questi, possa mandare in down il PlaystationNetwork per 1 giorno intero.");
		backEnder.setDanger(Danger.FATALERROR);
		backEnder.setHp(150);
		backEnder.setAtk(40);
		backEnder.setDef(14);
		backEnder.setSpeed(30);
		backEnder.setImageUrl("https://i.imgur.com/vkoBPs9.png"); // URL Back-Ender
		mDao.save(backEnder);

		Action baseBack= new Action();
		baseBack.setActionType(ActionType.BASE);
		baseBack.setNameAction("Fendente Oscuro");
		baseBack.setMaxNumTarget(1);
		baseBack.setDescriptionAction("Un colpo rapido e potente che taglia le linee di codice come burro.");
		baseBack.setPrecision(100);
		baseBack.setEntity(backEnder);
		aDao.save(baseBack);

		Action specialeBack= new Action();
		specialeBack.setActionType(ActionType.SPECIALE);
		specialeBack.setMolt(0.75); // Moltiplicatore < 1 per attacco ad area
		specialeBack.setCooldown(3);
		specialeBack.setNameAction("ExceptionSlash"); // Nome azione corretto
		specialeBack.setMaxNumTarget(3); // Colpisce tutti gli avversari (max 3 nel gioco?)
		specialeBack.setDescriptionAction("Il Back-Ender scatena un'eccezione non gestita che si propaga a tutti i nemici vicini.");
		specialeBack.setPrecision(95);
		specialeBack.setEntity(backEnder);
		aDao.save(specialeBack);


		// --- Mostro SourceCode ---
		Monster sourceCode = new Monster();
		sourceCode.setName("CodeSource"); // Nome corretto
		sourceCode.setDescription("Stringhe di codice verde su sfondo nero scorrono, prendendo la forma che hanno gli incubi. Un terrificante sviluppatore Zoom ha preso il controllo del codice binario, la base di tutta l'informatica, dandogli la forma di un drago per distruggere ogni cosa, e infine terminare se stesso, per porre fine all'informatica e rimandare la civiltà indietro di centinaia di anni.");
		sourceCode.setDanger(Danger.BLUESCREEN);
		sourceCode.setHp(300); // HP aumentato come da descrizione
		sourceCode.setAtk(50);
		sourceCode.setDef(20);
		sourceCode.setSpeed(50);
		sourceCode.setImageUrl("https://i.imgur.com/JiSd2Lr.png"); // URL CodeSource
		mDao.save(sourceCode);

		Action baseSource= new Action();
		baseSource.setActionType(ActionType.BASE);
		baseSource.setNameAction("Binary Blast");
		baseSource.setMaxNumTarget(1);
		baseSource.setDescriptionAction("Un raggio concentrato di puro codice binario colpisce il bersaglio.");
		baseSource.setPrecision(100);
		baseSource.setEntity(sourceCode);
		aDao.save(baseSource);

		Action specialeSource= new Action();
		specialeSource.setActionType(ActionType.SPECIALE);
		specialeSource.setMolt(1); // Moltiplicatore 1 per attacco ad area potente
		specialeSource.setCooldown(3);
		specialeSource.setNameAction("W0RLD 3ND3R"); // Nome azione corretto
		specialeSource.setMaxNumTarget(3); // Colpisce tutti (max 3?)
		specialeSource.setDescriptionAction("Il codice sorgente scatena un'ondata di corruzione binaria che minaccia di cancellare l'esistenza stessa.");
		specialeSource.setPrecision(100);
		specialeSource.setEntity(sourceCode);
		aDao.save(specialeSource);


	}
}
