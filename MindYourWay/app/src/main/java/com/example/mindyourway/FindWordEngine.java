package com.example.mindyourway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class FindWordEngine {
    ///tine de timer
    private int Minute;
    private int Secunde;

    ///tine de status
    private int StatusX;
    private int StatusY;

    ///tine de cuvintele pe care trebuie sa le bag  ###StatusX e cel curent
    private ArrayList<String> ListaCuvinteJoc;

    ///tine de mesaj joc
    private String MesajJoc;

    ///tine de matricea cu litere unde am si cuvantul
    private char[][] MatriceLitere;

    ///tine de dificultatea pe care o selectez
    private int dificultate;

    ///tine cont de starea in care se afla jocul 0 pentru se joaca 1 pentru game over si 2 pentru you win
    private int StareJoc;

    ///Constructori
    public FindWordEngine() {
        Minute = 0;
        Secunde = 0;
        StatusX = 0;
        StatusY = 0;
        ListaCuvinteJoc = new ArrayList<String>();
        MesajJoc = " FIND WORD";
        MatriceLitere = new char[10][10];
        dificultate = 0;
        StareJoc = 0;
        for(int i = 1; i <= 9 ;++i) {
            for (int j = 1; j <= 9; ++j) {
                MatriceLitere[i][j] = 0;
            }
        }
    }


    public FindWordEngine(FindWordEngine X) {
        this.MatriceLitere = new char [10][10];
        this.Minute = X.getMinute();
        this.Secunde = X.getSecunde();
        this.StatusX = X.getStatusX();
        this.StatusY = X.getStatusY();
        this.ListaCuvinteJoc = X.getListaCuvinteJoc();
        this.MesajJoc = X.getMesajJoc();
        this.StareJoc = X.getStareJoc();
        char [][] table = X.getMatriceLitere();

        for(int i = 1; i <= 9 ;++i) {
            for (int j = 1; j <= 9; ++j) {
                this.MatriceLitere[i][j] = table[i][j];
            }
        }

        this.dificultate = X.getDificultate();
    }

    public FindWordEngine(int dificultateNoua) {
        Minute = 0;
        Secunde = 0;
        StatusX = 0;
        StatusY = 0;
        dificultate = dificultateNoua;
        ListaCuvinteJoc = new ArrayList<String>();
        MesajJoc = " FIND WORD";
        StareJoc = 0;
        MatriceLitere = new char[10][10];
        for(int i = 0; i <= 9 ;++i) {
            for (int j = 0; j <= 9; ++j) {
                MatriceLitere[i][j] = 0;
            }
        }

        if(getDificultate() == 0) {
            SetMinute(6);
            SetSecunde(0);
            SetStatusX(0);
            SetStatusY(6);
        }
        else if(getDificultate() == 1) {
            SetMinute(5);
            SetSecunde(0);
            SetStatusX(0);
            SetStatusY(7);
        }
        else if(getDificultate() == 2) {
            SetMinute(4);
            SetSecunde(30);
            SetStatusX(0);
            SetStatusY(8);
        }
        generateListaCuvinteJoc();
    }

    public boolean isCompleted() {
        return StareJoc == 2;
    }

    ///si asta e scrisa e pentru un obiect
    public FindWordEngine getGame() {
        FindWordEngine res = new FindWordEngine(this);
        return res;
    }

    /// aici voi avea set-uri
    public void SetDificultate(int dificultateNoua) {
        dificultate = dificultateNoua;
    }

    public void SetMinute(int MinuteNoua) {
        Minute = MinuteNoua;
    }

    public void SetSecunde(int SecundeNoua) {
        Secunde = SecundeNoua;
    }

    public void SetStatusX(int StatusXNoua) {
        StatusX = StatusXNoua;
    }

    public void SetStatusY(int StatusYNoua) {
        StatusY = StatusYNoua;
    }

    public void IncreaseStatusX() {
        StatusX ++;
    }

    public void SetMesajJoc(String MesajJocNoua) {
        MesajJoc = MesajJocNoua;
    }

    public void SetStareJoc(int StareJocNoua) {
        StareJoc = StareJocNoua;
    }

    ///aici sunt functii care ajuta la joc
    public void generateLitere() {
        for(int i = 1; i <= 9 ;++i) {
            for (int j = 1; j <= 9; ++j) {
                MatriceLitere[i][j] = (char)((Math.abs(ThreadLocalRandom.current().nextInt()) % 26)+'A');
            }
        }
    }

    public void generateListaCuvinteJoc() {
        ArrayList<String> ListaCuvinte = new ArrayList<String>();

        ListaCuvinte.add("    apus  ");
        ListaCuvinte.add("    atom  ");
        ListaCuvinte.add("    baie  ");
        ListaCuvinte.add("    bere  ");
        ListaCuvinte.add("    card  ");
        ListaCuvinte.add("    ceai  ");
        ListaCuvinte.add("    cerb  ");
        ListaCuvinte.add("    cerc  ");
        ListaCuvinte.add("    cort  ");
        ListaCuvinte.add("    crab  ");
        ListaCuvinte.add("    curs  ");
        ListaCuvinte.add("    dans  ");
        ListaCuvinte.add("    epic  ");
        ListaCuvinte.add("    erou  ");
        ListaCuvinte.add("    fire  ");
        ListaCuvinte.add("    geam  ");
        ListaCuvinte.add("    glob  ");
        ListaCuvinte.add("    mici  ");
        ListaCuvinte.add("    oaie  ");
        ListaCuvinte.add("    ochi  ");
        ListaCuvinte.add("    opus  ");
        ListaCuvinte.add("    parc  ");
        ListaCuvinte.add("    plus  ");
        ListaCuvinte.add("    real  ");
        ListaCuvinte.add("    romb  ");
        ListaCuvinte.add("    tanc  ");
        ListaCuvinte.add("    tren  ");
        ListaCuvinte.add("    ulei  ");
        ListaCuvinte.add("    unde  ");
        ListaCuvinte.add("   arici  ");
        ListaCuvinte.add("   avion  ");
        ListaCuvinte.add("   balet  ");
        ListaCuvinte.add("   bazin  ");
        ListaCuvinte.add("   beton  ");
        ListaCuvinte.add("   bezea  ");
        ListaCuvinte.add("   bizon  ");
        ListaCuvinte.add("   botez  ");
        ListaCuvinte.add("   bronz  ");
        ListaCuvinte.add("   carte  ");
        ListaCuvinte.add("   ceaun  ");
        ListaCuvinte.add("   cidru  ");
        ListaCuvinte.add("   copil  ");
        ListaCuvinte.add("   coral  ");
        ListaCuvinte.add("   cruce  ");
        ListaCuvinte.add("   cutie  ");
        ListaCuvinte.add("   dolar  ");
        ListaCuvinte.add("   dosar  ");
        ListaCuvinte.add("   dulap  ");
        ListaCuvinte.add("   ecler  ");
        ListaCuvinte.add("   exact  ");
        ListaCuvinte.add("   fazan  ");
        ListaCuvinte.add("   ficat  ");
        ListaCuvinte.add("   flaut  ");
        ListaCuvinte.add("   folie  ");
        ListaCuvinte.add("   forum  ");
        ListaCuvinte.add("   homar  ");
        ListaCuvinte.add("   joker  ");
        ListaCuvinte.add("   lalea  ");
        ListaCuvinte.add("   lapte  ");
        ListaCuvinte.add("   liber  ");
        ListaCuvinte.add("   magic  ");
        ListaCuvinte.add("   maxim  ");
        ListaCuvinte.add("   mesaj  ");
        ListaCuvinte.add("   metal  ");
        ListaCuvinte.add("   miere  ");
        ListaCuvinte.add("   miner  ");
        ListaCuvinte.add("   minge  ");
        ListaCuvinte.add("   mobil  ");
        ListaCuvinte.add("   model  ");
        ListaCuvinte.add("   munte  ");
        ListaCuvinte.add("   muzeu  ");
        ListaCuvinte.add("   nisip  ");
        ListaCuvinte.add("   ocean  ");
        ListaCuvinte.add("   panou  ");
        ListaCuvinte.add("   pavaj  ");
        ListaCuvinte.add("   piele  ");
        ListaCuvinte.add("   pilot  ");
        ListaCuvinte.add("   plumb  ");
        ListaCuvinte.add("   polen  ");
        ListaCuvinte.add("   robot  ");
        ListaCuvinte.add("   rodie  ");
        ListaCuvinte.add("   salam  ");
        ListaCuvinte.add("   salon  ");
        ListaCuvinte.add("   salsa  ");
        ListaCuvinte.add("   sanie  ");
        ListaCuvinte.add("   sinus  ");
        ListaCuvinte.add("   soare  ");
        ListaCuvinte.add("   somon  ");
        ListaCuvinte.add("   spirt  ");
        ListaCuvinte.add("   sport  ");
        ListaCuvinte.add("   stare  ");
        ListaCuvinte.add("   steag  ");
        ListaCuvinte.add("   stres  ");
        ListaCuvinte.add("   vechi  ");
        ListaCuvinte.add("   video  ");
        ListaCuvinte.add("   virus  ");
        ListaCuvinte.add("   volan  ");
        ListaCuvinte.add("   volei  ");
        ListaCuvinte.add("   vreme  ");
        ListaCuvinte.add("   accent ");
        ListaCuvinte.add("   alcool ");
        ListaCuvinte.add("   amidon ");
        ListaCuvinte.add("   aparat ");
        ListaCuvinte.add("   argint ");
        ListaCuvinte.add("   asasin ");
        ListaCuvinte.add("   asfalt ");
        ListaCuvinte.add("   biblie ");
        ListaCuvinte.add("   biceps ");
        ListaCuvinte.add("   breloc ");
        ListaCuvinte.add("   burete ");
        ListaCuvinte.add("   cactus ");
        ListaCuvinte.add("   cangur ");
        ListaCuvinte.add("   castor ");
        ListaCuvinte.add("   caviar ");
        ListaCuvinte.add("   cimbru ");
        ListaCuvinte.add("   ciment ");
        ListaCuvinte.add("   ciocan ");
        ListaCuvinte.add("   coafor ");
        ListaCuvinte.add("   costum ");
        ListaCuvinte.add("   creier ");
        ListaCuvinte.add("   cuptor ");
        ListaCuvinte.add("   defect ");
        ListaCuvinte.add("   delfin ");
        ListaCuvinte.add("   diabet ");
        ListaCuvinte.add("   dragon ");
        ListaCuvinte.add("   editor ");
        ListaCuvinte.add("   femeie ");
        ListaCuvinte.add("   fistic ");
        ListaCuvinte.add("   floare ");
        ListaCuvinte.add("   fotbal ");
        ListaCuvinte.add("   fulger ");
        ListaCuvinte.add("   fundal ");
        ListaCuvinte.add("   furtun ");
        ListaCuvinte.add("   gutuie ");
        ListaCuvinte.add("   hectar ");
        ListaCuvinte.add("   hibrid ");
        ListaCuvinte.add("   iepure ");
        ListaCuvinte.add("   jurnal ");
        ListaCuvinte.add("   macara ");
        ListaCuvinte.add("   median ");
        ListaCuvinte.add("   mozaic ");
        ListaCuvinte.add("   parfum ");
        ListaCuvinte.add("   peisaj ");
        ListaCuvinte.add("   pensie ");
        ListaCuvinte.add("   pepene ");
        ListaCuvinte.add("   picior ");
        ListaCuvinte.add("   pijama ");
        ListaCuvinte.add("   poezie ");
        ListaCuvinte.add("   portal ");
        ListaCuvinte.add("   porumb ");
        ListaCuvinte.add("   public ");
        ListaCuvinte.add("   record ");
        ListaCuvinte.add("   rucsac ");
        ListaCuvinte.add("   saltea ");
        ListaCuvinte.add("   schimb ");
        ListaCuvinte.add("   serial ");
        ListaCuvinte.add("   spital ");
        ListaCuvinte.add("   stilou ");
        ListaCuvinte.add("   tablou ");
        ListaCuvinte.add("   trafic ");
        ListaCuvinte.add("   trapez ");
        ListaCuvinte.add("   traseu ");
        ListaCuvinte.add("   tricou ");
        ListaCuvinte.add("   vierme ");
        ListaCuvinte.add("   violet ");
        ListaCuvinte.add("   vopsea ");
        ListaCuvinte.add("   zodiac ");
        ListaCuvinte.add("  afacere ");
        ListaCuvinte.add("  alergie ");
        ListaCuvinte.add("  aliment ");
        ListaCuvinte.add("  amoniac ");
        ListaCuvinte.add("  arsenal ");
        ListaCuvinte.add("  atelier ");
        ListaCuvinte.add("  automat ");
        ListaCuvinte.add("  baclava ");
        ListaCuvinte.add("  baschet ");
        ListaCuvinte.add("  baterie ");
        ListaCuvinte.add("  binoclu ");
        ListaCuvinte.add("  buletin ");
        ListaCuvinte.add("  busuioc ");
        ListaCuvinte.add("  cafenea ");
        ListaCuvinte.add("  canapea ");
        ListaCuvinte.add("  ciclism ");
        ListaCuvinte.add("  colagen ");
        ListaCuvinte.add("  coloare ");
        ListaCuvinte.add("  comisar ");
        ListaCuvinte.add("  complex ");
        ListaCuvinte.add("  cozonac ");
        ListaCuvinte.add("  depozit ");
        ListaCuvinte.add("  diamant ");
        ListaCuvinte.add("  diferit ");
        ListaCuvinte.add("  difuzor ");
        ListaCuvinte.add("  dovleac ");
        ListaCuvinte.add("  ecograf ");
        ListaCuvinte.add("  elefant ");
        ListaCuvinte.add("  felinar ");
        ListaCuvinte.add("  fotoliu ");
        ListaCuvinte.add("  galerie ");
        ListaCuvinte.add("  ghiocel ");
        ListaCuvinte.add("  ghiveci ");
        ListaCuvinte.add("  hamster ");
        ListaCuvinte.add("  handbal ");
        ListaCuvinte.add("  hanorac ");
        ListaCuvinte.add("  hexagon ");
        ListaCuvinte.add("  iasomie ");
        ListaCuvinte.add("  istorie ");
        ListaCuvinte.add("  leopard ");
        ListaCuvinte.add("  magazie ");
        ListaCuvinte.add("  magazin ");
        ListaCuvinte.add("  mamifer ");
        ListaCuvinte.add("  manager ");
        ListaCuvinte.add("  mecanic ");
        ListaCuvinte.add("  militar ");
        ListaCuvinte.add("  monitor ");
        ListaCuvinte.add("  mucenic ");
        ListaCuvinte.add("  orgoliu ");
        ListaCuvinte.add("  orhidee ");
        ListaCuvinte.add("  palmier ");
        ListaCuvinte.add("  papagal ");
        ListaCuvinte.add("  paradox ");
        ListaCuvinte.add("  parcare ");
        ListaCuvinte.add("  parchet ");
        ListaCuvinte.add("  pelican ");
        ListaCuvinte.add("  pescuit ");
        ListaCuvinte.add("  piersic ");
        ListaCuvinte.add("  pinguin ");
        ListaCuvinte.add("  poluare ");
        ListaCuvinte.add("  poveste ");
        ListaCuvinte.add("  prenume ");
        ListaCuvinte.add("  protest ");
        ListaCuvinte.add("  pulover ");
        ListaCuvinte.add("  referat ");
        ListaCuvinte.add("  rinichi ");
        ListaCuvinte.add("  student ");
        ListaCuvinte.add("  subiect ");
        ListaCuvinte.add("  tractor ");
        ListaCuvinte.add("  tramvai ");
        ListaCuvinte.add("  usturoi ");
        ListaCuvinte.add("  virtual ");
        ListaCuvinte.add("  accident");
        ListaCuvinte.add("  admitere");
        ListaCuvinte.add("  albastru");
        ListaCuvinte.add("  aluminiu");
        ListaCuvinte.add("  ansamblu");
        ListaCuvinte.add("  arhitect");
        ListaCuvinte.add("  bancomat");
        ListaCuvinte.add("  calendar");
        ListaCuvinte.add("  cearceaf");
        ListaCuvinte.add("  cilindru");
        ListaCuvinte.add("  clarinet");
        ListaCuvinte.add("  comunism");
        ListaCuvinte.add("  curcubeu");
        ListaCuvinte.add("  cutremur");
        ListaCuvinte.add("  dormitor");
        ListaCuvinte.add("  draperie");
        ListaCuvinte.add("  fantezie");
        ListaCuvinte.add("  farfurie");
        ListaCuvinte.add("  farmacie");
        ListaCuvinte.add("  fericire");
        ListaCuvinte.add("  fotograf");
        ListaCuvinte.add("  frigider");
        ListaCuvinte.add("  genunchi");
        ListaCuvinte.add("  ghiozdan");
        ListaCuvinte.add("  horoscop");
        ListaCuvinte.add("  instinct");
        ListaCuvinte.add("  internet");
        ListaCuvinte.add("  labirint");
        ListaCuvinte.add("  magnolie");
        ListaCuvinte.add("  microfon");
        ListaCuvinte.add("  patinoar");
        ListaCuvinte.add("  pensiune");
        ListaCuvinte.add("  portofel");
        ListaCuvinte.add("  revelion");
        ListaCuvinte.add("  rezervor");
        ListaCuvinte.add("  scripete");
        ListaCuvinte.add("  strugure");
        ListaCuvinte.add("  talisman");
        ListaCuvinte.add("  telescop");
        ListaCuvinte.add("  tensiune");
        ListaCuvinte.add(" amortizor");
        ListaCuvinte.add(" antistres");
        ListaCuvinte.add(" antivirus");
        ListaCuvinte.add(" automobil");
        ListaCuvinte.add(" calorifer");
        ListaCuvinte.add(" economist");
        ListaCuvinte.add(" hamburger");
        ListaCuvinte.add(" libertate");
        ListaCuvinte.add(" mesagerie");
        ListaCuvinte.add(" navigator");
        ListaCuvinte.add(" pandantiv");
        ListaCuvinte.add(" portbagaj");
        ListaCuvinte.add(" prietenie");
        ListaCuvinte.add(" realitate");
        ListaCuvinte.add(" republica");
        ListaCuvinte.add(" reumatism");
        ListaCuvinte.add(" simulator");
        ListaCuvinte.add(" televizor");
        ListaCuvinte.add(" testament");
        ListaCuvinte.add(" traducere");
        ListaCuvinte.add(" trandafir");

        ArrayList<String> ListaCuvinteSelectate = new ArrayList<String>();

        if(dificultate == 0) {
            for ( int i = 0; i < 100 ; ++i) {
                ListaCuvinteSelectate.add(String.valueOf(ListaCuvinte.get(i)));
            }
        }
        else if(dificultate == 1) {
            for ( int i = 100; i < 200 ; ++i) {
                ListaCuvinteSelectate.add(String.valueOf(ListaCuvinte.get(i)));
            }
        }
        else if(dificultate == 2) {
            for ( int i = 200; i < 300 ; ++i) {
                ListaCuvinteSelectate.add(String.valueOf(ListaCuvinte.get(i)));
            }
        }

        Collections.shuffle(ListaCuvinteSelectate);

        for( int i = 0 ; i < StatusY ; ++i) {
            ListaCuvinteJoc.add(String.valueOf(ListaCuvinteSelectate.get(i)));
        }
    }

    private int putOrizontal(int x,int y,String Cuvant) {
        if(y + Cuvant.length() - 1 <= 9) {
            for( int j = y; j <= y + Cuvant.length() - 1 ; ++j) {
                MatriceLitere[x][j] = Cuvant.charAt(j-y);
            }
            return 1;
        }
        return 0;
    }

    private int putVertical(int x,int y,String Cuvant) {
        if(x + Cuvant.length() - 1 <= 9) {
            for( int i = x; i <= x + Cuvant.length() - 1 ; ++i) {
                MatriceLitere[i][y] = Cuvant.charAt(i-x);
            }
            return 1;
        }
        return 0;
    }

    public void adaugaCuvant(String Cuvant) {
        String CuvantFaraSpatii = "";
        for(int i = 0; i < Cuvant.length(); ++i) {
            if(String.valueOf(Cuvant.charAt(i)).contains(" "));
            else {
                CuvantFaraSpatii += String.valueOf(Cuvant.charAt(i));
            }
        }

        int ok = 1;
        while(ok == 1) {
            int indexI = (Math.abs(ThreadLocalRandom.current().nextInt()) % 9) + 1;
            int indexJ = (Math.abs(ThreadLocalRandom.current().nextInt()) % 9) + 1;

            int op = (Math.abs(ThreadLocalRandom.current().nextInt()) % 2) + 1;

            if(op == 1) {
                if(putOrizontal(indexI,indexJ,CuvantFaraSpatii) != 0) ok = 0;
            }
            else if(op == 2) {
                if(putVertical(indexI,indexJ,CuvantFaraSpatii) != 0) ok = 0;
            }
        }
    }

    private int TryOrizontal(int Sx,int Sy,int Fx,int Fy,String Cuvant) {
        if(Sx != Fx) {
            return 0;
        }

        if(Fy - Sy + 1 != Cuvant.length()) {
            return 0;
        }

        for( int j = Sy; j <= Fy ; ++j) {
            if(String.valueOf(Cuvant.charAt(j-Sy)).contains(String.valueOf(MatriceLitere[Sx][j])));
            else {
                return 0;
            }
        }

        return 1;
    }

    private int TryVertical(int Sx,int Sy,int Fx,int Fy,String Cuvant) {
        if(Sy != Fy) {
            return 0;
        }

        if(Fx - Sx + 1 != Cuvant.length()) {
            return 0;
        }

        for( int i = Sx; i <= Fx ; ++i) {
            if(String.valueOf(Cuvant.charAt(i-Sx)).contains(String.valueOf(MatriceLitere[i][Sy])));
            else {
                return 0;
            }
        }

        return 2;
    }

    public int checkPotrivireCuvant(int Sx,int Sy,int Fx,int Fy,String Cuvant) {
        String CuvantFaraSpatii = "";
        for(int i = 0; i < Cuvant.length(); ++i) {
            if(String.valueOf(Cuvant.charAt(i)).contains(" "));
            else {
                CuvantFaraSpatii += String.valueOf(Cuvant.charAt(i));
            }
        }

        if(TryOrizontal(Sx,Sy,Fx,Fy,CuvantFaraSpatii) == 1) {
            return 1;
        }
        else if(TryVertical(Sx,Sy,Fx,Fy,CuvantFaraSpatii) == 2) {
            return 2;
        }

        return 0;
    }


    ///Aici voi avea get pentru tot

    public int getDificultate() {
        return dificultate;
    }

    public int getMinute() {
        return Minute;
    }

    public int getSecunde() {
        return Secunde;
    }

    public int getStatusX() {
        return StatusX;
    }

    public int getStatusY() {
        return StatusY;
    }

    public String getMesajJoc() {
        return MesajJoc;
    }

    public int getStareJoc() {
        return StareJoc;
    }

    public ArrayList<String> getListaCuvinteJoc() {
        ArrayList<String> res = new ArrayList<String>();
        for(int i = 0; i < ListaCuvinteJoc.size() ; ++i) {
            res.add(String.valueOf(ListaCuvinteJoc.get(i)));
        }
        return res;
    }

    public String getCuvantX(int X) {
        return String.valueOf(ListaCuvinteJoc.get(X));
    }

    public char[][] getMatriceLitere() {
        char[][] res = new char[10][10];

        for(int i = 1; i <= 9 ;++i) {
            for (int j = 1; j <= 9; ++j) {
                res[i][j] = MatriceLitere[i][j];
            }
        }

        return res;
    }
}

