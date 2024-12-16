package menu.personsuche;

import data.user.UserName;

import java.util.Arrays;

public enum PersonSucheMenuOption {

 PINNWAND("Pinnwand von %s",1),
 CHAT("Chat mit %s",2),
 ZURUECK("zurück",0);


 private final int wahlnummer;
 private final String text;

 PersonSucheMenuOption(String text,int wahlnummer) {
     this.wahlnummer = wahlnummer;
     this.text = text;
 }

 public static void printAll(UserName userName){
     Arrays.stream(PersonSucheMenuOption.values()).forEach(o -> System.out.println(o.wahlnummer+": "+o.text.formatted(userName)));
 }


public static PersonSucheMenuOption ofWahlnummer(int wahlnummer){
    return Arrays.stream(PersonSucheMenuOption.values()).filter(o -> o.wahlnummer == wahlnummer).findFirst().orElse(null);
}


public int getWahlnummer() {
 return wahlnummer;
}




}
