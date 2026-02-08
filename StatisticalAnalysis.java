package statistical_analysis;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

public class statistical_analysis; {

    private static List<Double> bagimsizDegisken1=new ArrayList<>();//alan
    private static List<Double> bagimsizDegisken2=new ArrayList<>();//yatakodası sayısı
    private static List<Double> bagimsizDegisken3=new ArrayList<>();//banyo sayısı
    private static List<Double> bagimsizDegisken4=new ArrayList<>();//kat sayısı
    private static List<Double> bagimsizDegisken5=new ArrayList<>();//otopark sayısı
    private static List<Double> bagimliDegisken=new ArrayList<>();//ev fiyatı
    private static List<List<List<Double>>> veriler=new ArrayList<>();
    private static List<List<Double>> egitimSetleri=new ArrayList<>();
    private static List<List<Double>> testSetleri=new ArrayList<>();
    
    private static List<Double> egitimSeti1=new ArrayList<>();
    private static List<Double> testSeti1=new ArrayList<>();
    
    private static List<Double> egitimSeti2=new ArrayList<>();
    private static List<Double> testSeti2=new ArrayList<>();
    
    private static List<Double> egitimSeti3=new ArrayList<>();
    private static List<Double> testSeti3=new ArrayList<>();
    
    private static List<Double> egitimSeti4=new ArrayList<>();
    private static List<Double> testSeti4=new ArrayList<>();
    
    private static List<Double> egitimSeti5=new ArrayList<>();
    private static List<Double> testSeti5=new ArrayList<>();
    
    private static List<Double> yEgitimSeti=new ArrayList<>();
    private static List<Double> yTestSeti=new ArrayList<>();
    
    private static double r1,r2,r3,r4,r5;
    private static List<Double> korelasyonkatsayilari=new ArrayList<>();
    private static double aDegeri,bDegeri;
    private static List<Double> egitimVerileriTahminiDegerler=new ArrayList<>();
    private static List<Double> testVerileriTahminiDegerler=new ArrayList<>();
    private static double egitimSSE;
    private static double testSSE;
    private static double[] sseDegerleri;
    
    public static void main(String[] args) {
        
        String dosya="Housing.csv";
        try(BufferedReader br=new BufferedReader(new FileReader(dosya)))
        {
            String satir;
            int okundu=0;
            int sayac=0;
            if((satir=br.readLine())!=null)
            {
                okundu=1;
            }
            if(okundu!=0)
            {
                while((satir=br.readLine())!=null && sayac<100)
                {
                    String[] veriler=satir.split(",");
                    for(int i=0;i<veriler.length;i++)
                    {
                        if(i==0)
                        {
                            bagimliDegisken.add(Double.parseDouble(veriler[i].trim()));
                        }
                        else if(i==1)
                        {
                            bagimsizDegisken1.add(Double.parseDouble(veriler[i].trim()));
                        }
                        else if(i==2)
                        {
                            bagimsizDegisken2.add(Double.parseDouble(veriler[i].trim()));
                        }
                        else if(i==3)
                        {
                            bagimsizDegisken3.add(Double.parseDouble(veriler[i].trim()));
                        }
                        else if(i==4)
                        {
                            bagimsizDegisken4.add(Double.parseDouble(veriler[i].trim()));
                        }
                        else if(i==10)
                        {
                            bagimsizDegisken5.add(Double.parseDouble(veriler[i].trim()));
                        }
                    }
                    sayac++;
                }
            }
            veriler=egitimvetestVeriSetiOlusturma(bagimsizDegisken1,bagimsizDegisken2,bagimsizDegisken3,bagimsizDegisken4,bagimsizDegisken5,bagimliDegisken);
            
            egitimSetleri=veriler.get(0);
            testSetleri=veriler.get(1);
            int sutunSayisi1;
            int sutunSayisi2;
            List<List<Double>> degiskenListeleri1 = new ArrayList<>();
            List<List<Double>> degiskenListeleri2 = new ArrayList<>();
            if(!egitimSetleri.isEmpty())
            {
                sutunSayisi1=egitimSetleri.get(0).size();
            }
            else
            {
                sutunSayisi1=0;
            }
            if(!testSetleri.isEmpty())
            {
                sutunSayisi2=testSetleri.get(0).size();
            }
            else
            {
                sutunSayisi2=0;
            }
            
            
            for(int i=0;i<sutunSayisi1;i++)
            {
                int finalI=i;
                List<Double> degiskenEgitimListesi = egitimSetleri.stream().map(row -> row.get(finalI)).toList();
                degiskenListeleri1.add(degiskenEgitimListesi);
            }
            if(!degiskenListeleri1.isEmpty())
            {
                egitimSeti1.addAll(degiskenListeleri1.get(0));
                egitimSeti2.addAll(degiskenListeleri1.get(1));
                egitimSeti3.addAll(degiskenListeleri1.get(2));
                egitimSeti4.addAll(degiskenListeleri1.get(3));
                egitimSeti5.addAll(degiskenListeleri1.get(4));
                yEgitimSeti.addAll(degiskenListeleri1.get(5));
            }
            
            for(int i=0;i<sutunSayisi2;i++)
            {
                int finalI=i;
                List<Double> degiskenTestListesi =testSetleri.stream().map(row -> row.get(finalI)).toList();
                degiskenListeleri2.add(degiskenTestListesi);
            }
            
            if(!degiskenListeleri2.isEmpty())
            {
                testSeti1.addAll(degiskenListeleri2.get(0));
                testSeti2.addAll(degiskenListeleri2.get(1));
                testSeti3.addAll(degiskenListeleri2.get(2));
                testSeti4.addAll(degiskenListeleri2.get(3));
                testSeti5.addAll(degiskenListeleri2.get(4));
                yTestSeti.addAll(degiskenListeleri2.get(5));
            }
            
            r1=pearsonKorelasyonKatsayisi(egitimSeti1,yEgitimSeti);
            korelasyonkatsayilari.add(r1);
            r2=pearsonKorelasyonKatsayisi(egitimSeti2,yEgitimSeti);
            korelasyonkatsayilari.add(r2);
            r3=pearsonKorelasyonKatsayisi(egitimSeti3,yEgitimSeti);
            korelasyonkatsayilari.add(r3);
            r4=pearsonKorelasyonKatsayisi(egitimSeti4,yEgitimSeti);
            korelasyonkatsayilari.add(r4);
            r5=pearsonKorelasyonKatsayisi(egitimSeti5,yEgitimSeti);
            korelasyonkatsayilari.add(r5);
            
            for(int i=0;i<korelasyonkatsayilari.size();i++)
            {
                System.out.println("r"+(i+1)+" korelasyon katsayisi: "+korelasyonkatsayilari.get(i));
            }
            
            double[] enYuksekKorelasyonDizisi=enYuksekKorelasyonKatsayisi(korelasyonkatsayilari);
            double deger=enYuksekKorelasyonDizisi[0];
            int index=(int)enYuksekKorelasyonDizisi[1];
            System.out.println("En Yuksek Korelasyon Katsayisi r"+(index+1)+"'dir: "+deger);
            if(index==0)
            {
                bDegeri=bRegresyonKatsayisiHesaplama(egitimSeti1,yEgitimSeti);
                aDegeri=aRegresyonSabitiHesaplama(egitimSeti1,yEgitimSeti,bDegeri);
                egitimVerileriTahminiDegerler=tahminiDegerler(egitimSeti1, testSeti1,aDegeri,bDegeri).get(0);
                testVerileriTahminiDegerler=tahminiDegerler(egitimSeti1, testSeti1,aDegeri,bDegeri).get(1);
            }
            else if(index==1)
            {
                bDegeri=bRegresyonKatsayisiHesaplama(egitimSeti2,yEgitimSeti);
                aDegeri=aRegresyonSabitiHesaplama(egitimSeti2,yEgitimSeti,bDegeri);
                egitimVerileriTahminiDegerler=tahminiDegerler(egitimSeti2, testSeti2,aDegeri,bDegeri).get(0);
                testVerileriTahminiDegerler=tahminiDegerler(egitimSeti2, testSeti2,aDegeri,bDegeri).get(1);
            }
            else if(index==2)
            {
                bDegeri=bRegresyonKatsayisiHesaplama(egitimSeti3,yEgitimSeti);
                aDegeri=aRegresyonSabitiHesaplama(egitimSeti3,yEgitimSeti,bDegeri);
                egitimVerileriTahminiDegerler=tahminiDegerler(egitimSeti3, testSeti3,aDegeri,bDegeri).get(0);
                testVerileriTahminiDegerler=tahminiDegerler(egitimSeti3, testSeti3,aDegeri,bDegeri).get(1);
            }
            else if(index==3)
            {
                bDegeri=bRegresyonKatsayisiHesaplama(egitimSeti4,yEgitimSeti);
                aDegeri=aRegresyonSabitiHesaplama(egitimSeti4,yEgitimSeti,bDegeri);
                egitimVerileriTahminiDegerler=tahminiDegerler(egitimSeti4, testSeti4,aDegeri,bDegeri).get(0);
                testVerileriTahminiDegerler=tahminiDegerler(egitimSeti4, testSeti4,aDegeri,bDegeri).get(1);
            }
            else if(index==4)
            {
                bDegeri=bRegresyonKatsayisiHesaplama(egitimSeti5,yEgitimSeti);
                aDegeri=aRegresyonSabitiHesaplama(egitimSeti5,yEgitimSeti,bDegeri);
                egitimVerileriTahminiDegerler=tahminiDegerler(egitimSeti5, testSeti5,aDegeri,bDegeri).get(0);
                testVerileriTahminiDegerler=tahminiDegerler(egitimSeti5, testSeti5,aDegeri,bDegeri).get(1);
            }
            
            System.out.println("a regresyon sabiti: "+aDegeri);
            System.out.println("b regresyon katsayisi: "+bDegeri);
            System.out.println("Denklem  y="+aDegeri+"+"+bDegeri+"*x");
            System.out.println("Egitim veri seti icin tahmini degerler");
            for(int i=0;i<egitimVerileriTahminiDegerler.size();i++)
            {
                System.out.println(egitimVerileriTahminiDegerler.get(i));
            }
            System.out.println("Test veri seti icin tahmini degerler");
            for(int i=0;i<testVerileriTahminiDegerler.size();i++)
            {
                System.out.println(testVerileriTahminiDegerler.get(i));
            }
            
            sseDegerleri=sseDegeriHesaplama(egitimVerileriTahminiDegerler,testVerileriTahminiDegerler, yEgitimSeti, yTestSeti);
            System.out.println("Egitim verisi icin SSE degeri: "+sseDegerleri[0]);
            System.out.println("Test verisi icin SSE degeri: "+sseDegerleri[1]);
            
        }
        catch(IOException hata)
        {
            System.out.println("Dosya Okuma Hatasi: "+hata.getMessage());
        }
    }
    
    public static List<List<List<Double>>> egitimvetestVeriSetiOlusturma(List<Double> x1,List<Double> x2,List<Double> x3,List<Double> x4,List<Double> x5,List<Double> y)
    {
        int veriBoyutu=x1.size();
        List<List<Double>> veriler=new ArrayList<>();
        
        for(int i=0;i<veriBoyutu;i++)
        {
            veriler.add(List.of(x1.get(i),x2.get(i),x3.get(i),x4.get(i),x5.get(i),y.get(i)));
        }
        
        Collections.shuffle(veriler);
        int egitimVeriSayisi=(int)(veriler.size()*(0.7));
        List<List<Double>> egitimSeti=veriler.subList(0,egitimVeriSayisi);
        List<List<Double>> testSeti=veriler.subList(egitimVeriSayisi,veriler.size());
        
        return List.of(egitimSeti,testSeti);
    }
    public static double pearsonKorelasyonKatsayisi(List<Double> veriSeti,List<Double> y)
    {
        double toplamxiyi=0;
        double ortalamax;
        double ortalamay;
        int veriSayisi;
        double toplamxi2=0;
        double toplamyi2=0;
        double r;
        
        veriSayisi=veriSeti.size();
        
        for(int i=0;i<veriSayisi;i++)
        {
            toplamxiyi+=veriSeti.get(i)*y.get(i);
        }
        
        ortalamax=aritmetikOrtalamaHesaplama(veriSeti);
        ortalamay=aritmetikOrtalamaHesaplama(y);
        
        for(int i=0;i<veriSayisi;i++)
        {
            toplamxi2+=Math.pow(veriSeti.get(i),2);
            toplamyi2+=Math.pow(y.get(i),2);
        }
        
        double pay=(double)toplamxiyi-(veriSayisi*ortalamax*ortalamay);
        double payda=(double)Math.sqrt((toplamxi2-veriSayisi*Math.pow(ortalamax,2))*(toplamyi2-veriSayisi*Math.pow(ortalamay,2)));
        r=pay/payda;
        return r;
    }
    
    public static double aritmetikOrtalamaHesaplama(List<Double> veriSeti)
    {
        double toplam=0;
        double ortalama;
        int veriSayisi=veriSeti.size();
        for(int i=0;i<veriSayisi;i++)
        {
            toplam+=veriSeti.get(i);
        }
        
        ortalama=(double)toplam/veriSayisi;
        return ortalama;
    }
    
    public static double[] enYuksekKorelasyonKatsayisi(List<Double> katsayilar)
    {
        double enYuksek=katsayilar.get(0);
        double index=0;
        for(int i=0;i<katsayilar.size();i++)
        {
            if(katsayilar.get(i)>enYuksek)
            {
                enYuksek=katsayilar.get(i);
                index=i;
            }
        }
        
        double[] dizi=new double[2];
        dizi[0]=enYuksek;
        dizi[1]=Double.valueOf(index);
        
        return dizi;
        
    }
    
    public static double bRegresyonKatsayisiHesaplama(List<Double> x,List<Double> y)
    {
        double toplamxiyi=0;
        double toplamxi=0;
        double toplamyi=0;
        double toplamxi2=0;
        int n=x.size();
        
        for(int i=0;i<n;i++)
        {
            toplamxiyi+=x.get(i)*y.get(i);
            toplamxi+=x.get(i);
            toplamyi+=y.get(i);
            toplamxi2+=Math.pow(x.get(i),2);
        }
        
        double pay=(double)(n*toplamxiyi)-(toplamxi*toplamyi);
        double payda=(double)(n*toplamxi2)-(Math.pow(toplamxi,2));
        
        return (double)pay/payda;
    }
    
    public static double aRegresyonSabitiHesaplama(List<Double> x,List<Double> y,double b)
    {
        double ortalamax,ortalamay;
        double a;
        
        ortalamax=(double)aritmetikOrtalamaHesaplama(x);
        ortalamay=(double)aritmetikOrtalamaHesaplama(y);
        
        a=(double)ortalamay-(b*ortalamax);
        return a;
        
    }
    
    public static List<List<Double>> tahminiDegerler(List<Double> egitimVeriSeti,List<Double> testVeriSeti,double a,double b)
    {
        List<Double> veriler1=new ArrayList<>(egitimVeriSeti);
        List<Double> veriler2=new ArrayList<>(testVeriSeti);
        
        List<Double> egitimTahmini=new ArrayList<>();
        List<Double> testTahmini=new ArrayList<>();
        double egitimDeger;
        double testDeger;
        
        for(int i=0;i<veriler1.size();i++)
        {
            egitimDeger=(double)a+b*veriler1.get(i);
            egitimTahmini.add(egitimDeger);
        }
        
        for(int i=0;i<veriler2.size();i++)
        {
            testDeger=(double)a+b*veriler2.get(i);
            testTahmini.add(testDeger);
        }
        
        return List.of(egitimTahmini,testTahmini);
        
    }
    
    public static double[] sseDegeriHesaplama(List<Double> egitim,List<Double> test,List<Double> yEgitim,List<Double> yTest)
    {
        double toplamEgitim=0;
        double toplamTest=0;
        double[] dizi=new double[2];
        
        for(int i=0;i<yEgitim.size();i++)
        {
            toplamEgitim+=Math.pow((yEgitim.get(i)-egitim.get(i)),2);
        }
        
        for(int i=0;i<yTest.size();i++)
        {
            toplamTest+=Math.pow((yTest.get(i)-test.get(i)),2);
        }
        dizi[0]=toplamEgitim;
        dizi[1]=toplamTest;
        return dizi;
    }
}

