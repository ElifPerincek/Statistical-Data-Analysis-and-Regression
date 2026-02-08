package pkg231213098_ist_ve_iht_ödev2;

import java.io.*;
import java.util.*;

public class Main {

    List<List<Double>> xListeleri = new ArrayList<>(); 
    List<Double> y = new ArrayList<>();

    public Main() throws IOException {
        FileReader fr = new FileReader("veri_seti.txt");
        BufferedReader br = new BufferedReader(fr);

        String satir = br.readLine(); 
        if (satir != null) {
            String[] basliklar = satir.split(",");
            int xSayi = basliklar.length - 1; //
            
           
         
            
            for (int i = 0; i < xSayi; i++) {
                xListeleri.add(new ArrayList<>());
            }
        }

        while ((satir = br.readLine()) != null) {  
            String[] veriler = satir.split(",");
            try {
                for (int i = 0; i < xListeleri.size(); i++) {
                    xListeleri.get(i).add(Double.parseDouble(veriler[i]));
                }
                y.add(Double.parseDouble(veriler[veriler.length - 1]));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Hatalı satır atlandı: " + satir);
            }
        }
        br.close();
        
      

      
        List<Integer> indeksler = new ArrayList<>();
        for (int i = 0; i < y.size(); i++) {
            indeksler.add(i);
        }
        Collections.shuffle(indeksler, new Random());//karıstırmak için 

        int egitimBoyutu = (int) (y.size() * 0.7);

        
        List<List<Double>> egitimXListeleri = new ArrayList<>();//yine tam sayısını bilmediğimizden bu şekilde arraylist olusturduk
        for (int i = 0; i < xListeleri.size(); i++) {
            egitimXListeleri.add(new ArrayList<>());
        }
        List<Double> egitimY = new ArrayList<>();

     
        List<List<Double>> testXListeleri = new ArrayList<>();
        for (int i = 0; i < xListeleri.size(); i++) {
            testXListeleri.add(new ArrayList<>());
        }
        List<Double> testY = new ArrayList<>();

     
        for (int i = 0; i < egitimBoyutu; i++) {
            int idx = indeksler.get(i);
            for (int j = 0; j < xListeleri.size(); j++) {
                egitimXListeleri.get(j).add(xListeleri.get(j).get(idx));
            }
            egitimY.add(y.get(idx));
        }
        
     
        for (int i = egitimBoyutu; i < y.size(); i++) {
            int idx = indeksler.get(i);
            for (int j = 0; j < xListeleri.size(); j++) {
                testXListeleri.get(j).add(xListeleri.get(j).get(idx));
            }
            testY.add(y.get(idx));
        }
        
        System.out.println("Eğitim veri boyutu: " + egitimY.size() + " (%70)");
        System.out.println("Test veri boyutu: " + testY.size() + " (%30)");

       
        double maxR = Double.NEGATIVE_INFINITY; 
        int secilenIndeks = -1;

        for (int i = 0; i < egitimXListeleri.size(); i++) {
            double r = korelasyon(egitimXListeleri.get(i), egitimY);
            System.out.println("r" + (i + 1) + " (x" + (i + 1) + ", y): " + r);

           
            if (r > maxR) {
                maxR = r;
                secilenIndeks = i;
            }
        }

 
        if (secilenIndeks != -1) {
            List<Double> enIyiX = egitimXListeleri.get(secilenIndeks);
            String xiAdi = "x" + (secilenIndeks + 1);
            double b = hesaplaB(enIyiX, egitimY);
            double a = hesaplaA(enIyiX, egitimY, b);

            System.out.println("\nSeçilen bağımsız değişken: " + xiAdi);
            System.out.println("Regresyon modeli: y = " + a + " + " + b + " * " + xiAdi);

           
            double sseTrain = 0;
            System.out.println("\nEĞİTİM VERİSİ - Tahmini y değerleri:");
            for (int i = 0; i < enIyiX.size(); i++) {
                double tahmin = a + b * enIyiX.get(i);
                double gercek = egitimY.get(i);
                System.out.println("Gerçek y: " + gercek + ", Tahmini ŷ: " + tahmin);
                sseTrain += Math.pow((gercek - tahmin), 2);
            }

            System.out.println("\nEĞİTİM VERİSİ - SSE (Toplam Kare Hata): " + sseTrain);
            

            List<Double> testEnIyiX = testXListeleri.get(secilenIndeks);
            double sseTest = 0;
            
            System.out.println("\nTEST VERİSİ - Tahmini y değerleri:");
            for (int i = 0; i < testEnIyiX.size(); i++) {
                double tahmin = a + b * testEnIyiX.get(i);
                double gercek = testY.get(i);
                System.out.println("Gerçek y: " + gercek + ", Tahmini ŷ: " + tahmin);
                sseTest += Math.pow((gercek - tahmin), 2);
            }
            
            System.out.println("\nTEST VERİSİ - SSE (Toplam Kare Hata): " + sseTest);
            
        } else {
            System.out.println("Uygun bir bağımsız değişken bulunamadı.");
        }
    }

    public double korelasyon(List<Double> x, List<Double> y) {
        int n = x.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0, sumY2 = 0;

        for (int i = 0; i < n; i++) {
            sumX += x.get(i);
            sumY += y.get(i);
            sumXY += x.get(i) * y.get(i);
            sumX2 += x.get(i) * x.get(i);
            sumY2 += y.get(i) * y.get(i);
        }

        double pay = (n * sumXY) - (sumX * sumY);
        double payda = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));//karekok hesaplama
        return payda == 0 ? 0 : pay / payda;
    }

    public double hesaplaB(List<Double> x, List<Double> y) {
        int n = x.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        for (int i = 0; i < n; i++) {
            sumX += x.get(i);
            sumY += y.get(i);
            sumXY += x.get(i) * y.get(i);
            sumX2 += x.get(i) * x.get(i);
        }
        return (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
    }

    public double hesaplaA(List<Double> x, List<Double> y, double b) {
        double sumX = 0, sumY = 0;
        int n = x.size();
        for (int i = 0; i < n; i++) {
            sumX += x.get(i);
            sumY += y.get(i);
        }
        return (sumY - b * sumX) / n;
    }

    public static void main(String[] args) throws IOException {
        new Main();
    }
}