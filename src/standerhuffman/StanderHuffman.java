
package standerhuffman;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author TOSHIBA
 *
 **/ 
public class StanderHuffman {

    public static class List
    {
        ArrayList< String  > characters;
        ArrayList< Float   > probability;
        ArrayList< String > Code;
    /******************functions********************/   
        public List()
        {
            characters   = new ArrayList <String> ();
            probability  = new ArrayList <Float>  ();
            Code         = new ArrayList <String> ();   
        }
    }
    
    public static void SelectionSort(ArrayList<Float> prob , ArrayList<String>chara) {
        for (int i = 0; i < prob.size(); i++) {
            // find position of smallest num between (i + 1)th element and last element
            int pos = i;
            for (int j = i; j < prob.size(); j++) {
                if (prob.get(j) > prob.get(pos))
                    pos = j;
            }
            // Swap min (smallest num) to current position on array
            float max = prob.get(pos);
            prob.set(pos, prob.get(i)); // Change in array list
            prob.set(i, max);
            String maxchar = chara.get(pos);
            chara.set(pos, chara.get(i));
            chara.set(i, maxchar);
        }
      }
    
    public static List CalcProbability (String word)
    {
        int  T=0 ;
        float counter=0 ;
        ArrayList<Character>CheckList = new ArrayList<Character>();
        List original = new List() ;
        char Char;
        for(int i=1 ; i<=word.length() ; i++ )
        {
            Char=word.charAt(i-1);
            counter=0;
            for(int j=0 ; j<word.length() ;j++)
            {
                if(CheckList.contains(Char))
                {
                    T=1;
                    break;
                }
                else
                {
                    T=0;
                    if(Char==word.charAt(j))
                    {
                        counter++;
                    }
                    else
                    {
                        continue;
                    }
                } 
            }
            if(T==0)
            {
                CheckList.add(Char);
                String s=""+Char;
                original.characters.add(s);
                original.probability.add(counter/word.length());
            }      
        }
       // Collections.sort(original.probability , Collections.reverseOrder());
       SelectionSort( original.probability , original.characters);
               
     System.out.println("/***********************************Probability**********************************/");
     for(int c=0 ; c<original.characters.size() ; c++)
     {
         System.out.print(original.characters.get(c)+"  ------>  ");
         System.out.println(original.probability.get(c));
     }
     return original;
    }
    
    public static void compression (String word ,List table)
    {
        String CompressedData="";
        char Temp;
        for (int i=0 ; i<word.length() ; i++)
        {
            Temp=word.charAt(i);
            for(int j=0 ; j< table.characters.size() ; j++)
            {
                if(Temp==table.characters.get(j).charAt(0))
                {
                    CompressedData+=table.Code.get(j);
                }
            }
        }
        System.out.println("Compressed Data : " +CompressedData);
 /******************************************Put Data In File *********************************************************/
            try        //Put Compressed In File "Compressed.Txt"
               {
                   FileWriter FOut =new FileWriter("Compressed.txt");
                   FOut.write(CompressedData);
                   FOut.close();
               }
               catch (Exception ex)
               {
                   System.out.println("Error Message");
               }
    }
    
    public static void WriteInFile (List Table)
    {
         try    //Put Tags In File "Tags.Txt"
               {
                   FileWriter FOut =new FileWriter("Table.txt");
                   BufferedWriter bufferedWriter =new BufferedWriter(FOut);
                   for(int i=0; i<Table.Code.size() ;i++)
                   {
                       bufferedWriter.write(Table.characters.get(i));
                       bufferedWriter.write("_");
                       bufferedWriter.write(Table.Code.get(i));
                       bufferedWriter.write("\n");
                   }
                   bufferedWriter.close();
               }
               catch (Exception ex)
               {
                   System.out.println("Error Message");
               }
    }
    public static List ReadFromFile()
    {
        List Temo=new List();
            try    
              {
                  String line=null;
                  String po="";          //to can split string and get pointer and next char
                  File fileName =new File("Table.txt");
                  FileReader fileReader =  new FileReader(fileName);
                  BufferedReader bufferedReader =  new BufferedReader(fileReader);
                  while((line = bufferedReader.readLine()) != null) 
                  {
                      for(int t=0; t<line.length();t++)
                      {
                          if(line.charAt(t)=='_')
                          {
                              Temo.characters.add(po);
                              po="";
                          }
                          else
                          {
                               po+=line.charAt(t);
                          }
                      }
                            Temo.Code.add(po);
                            po="";
                  }       
              }
              catch (Exception ex)
              {
                  System.out.println("*-*Error Message");
              }
                  for(int i=0 ; i<Temo.Code.size() ; i++)
                  {
                      System.out.println("Temo.characters "+Temo.characters.get(i) +" Temo.Code "+Temo.Code.get(i));
       
                  }    
        return Temo;
    }
    public static void Decompression ()
    {
        List Table;
        String CompressedData="";
        String OriginalData="";
        String Temp="";
        Table=ReadFromFile();
  /***********************************Read Compressed Data From File ****************************************/
        try    //read Compressed from File "Compressed.Txt"
                    {
                        CompressedData= new String (Files.readAllBytes(Paths.get("Compressed.txt")));
                    }
                catch (Exception ex)
                      {
                        System.out.println("*-*Error Message");
                      }
        /************************************************************************************************/
        for(int i=0 ; i< CompressedData.length() ; i++)
        {
            Temp+=CompressedData.charAt(i);
            for(int j=0 ; j<Table.Code.size() ;j++)
            {
                if(Temp.equals(Table.Code.get(j)))
                {
                    OriginalData+=Table.characters.get(j);
                    Temp="";
                    break;
                }
            }
        }
        System.out.println("Original Data : " +OriginalData);
    }
    public static void StandardHuffman (String Text)
    {
       
         List Temp = new List();
         int count=0;
         ArrayList<List> ProbData = new ArrayList<List>();
         ProbData.add(CalcProbability(Text));
         if(ProbData.get(0).characters.size()== 1)
         {
             ProbData.get(ProbData.size()-1).Code.add("0");
         }
         else{
         while(true)
         {
             if(ProbData.get(ProbData.size()-1).probability.size()==2) //if last list have 2 element stop
             {
                 break;
             }
             else
             {
                 String TempChar;
                 float TempProb;
                 int probDataSize = ProbData.size()-1;
                 int CharSize = ProbData.get(probDataSize).characters.size()-1;
                 //System.out.println("Test Size of Characters : " + CharSize);
                 //System.out.println("Test Size of ProbData.size() : " + ProbData.size());
             /*********************sum last 2****************************************************/
                 TempChar=ProbData.get(probDataSize).characters.get(CharSize)+ProbData.get(probDataSize).characters.get(CharSize-1);
                 TempProb=ProbData.get(probDataSize).probability.get(CharSize)+ProbData.get(probDataSize).probability.get(CharSize-1);
                 Temp.characters.add(TempChar);
                 Temp.probability.add(TempProb);
                 for(int Y=0 ; Y< CharSize-1  && CharSize>0 ;Y++)  //put previous data but not put last 2
                 {
                      //System.out.println(ProbData.get(probDataSize).characters.get(Y));
                     Temp.characters.add ( ProbData.get(probDataSize).characters.get(Y));
                     Temp.probability.add( ProbData.get(probDataSize).probability.get(Y));
                 }
                 SelectionSort(Temp.probability , Temp.characters);
             }
             ProbData.add(Temp);
             Temp= new List();
             count++;
         }
         
              System.out.println("/***********************************Test220**********************************/");
            for (int x=0; x<ProbData.size() ; x++)
            {
                for(int c=0 ; c<ProbData.get(x).characters.size() ; c++)
                {   System.out.print(ProbData.get(x).characters.get(c)+"    ------>  ");
                    System.out.println(ProbData.get(x).probability.get(c));}
                System.out.println("-------------------------------------");
            }
/********************************************************** CODE **************************************************************/
        int J=0;
        int Position=0;
        ProbData.get(ProbData.size()-1).Code.add("0");
        ProbData.get(ProbData.size()-1).Code.add("1");
        System.out.println("ProbData.size() :" +ProbData.size());
        for(int i=ProbData.size()-1 ; i>0 ; i--)
        {
            for(int j=0 ; j<ProbData.get(i).characters.size() ;j++)
            {
                for(int y=0 ; y<ProbData.get(i-1).characters.size() ;y++)
                {
                    if(ProbData.get(i).characters.get(j)==ProbData.get(i-1).characters.get(y))
                    {
                        String Tem=ProbData.get(i).Code.get(j);
                        ProbData.get(i-1).Code.add(Tem);
                        J=0;
                        break;
                    }
                    else 
                    {
                       J=1; 
                    }
            
                }
                if(J==1)
                {
                    Position=j;     // position not found string in prevoius list
                }
            }
             if (ProbData.get(i).characters.get(Position).contains(ProbData.get(i-1).characters.get(ProbData.get(i-1).characters.size()-1)) || ProbData.get(i).characters.get(Position).contains(ProbData.get(i-1).characters.get(ProbData.get(i-1).characters.size()-2)))
                    {
                         String newCode;
                         newCode=ProbData.get(i).Code.get(Position);
                         ProbData.get(i-1).Code.add(newCode+0);
                         ProbData.get(i-1).Code.add(newCode+1);
                    } 
        }
               System.out.println("/***********************************Final**********************************/");
            for (int x=0; x<ProbData.size() ; x++)
            {
                for(int c=0 ; c<ProbData.get(x).Code.size() ; c++)
                {   System.out.print(ProbData.get(x).characters.get(c) +"    ------>  ");
                    System.out.print(ProbData.get(x).probability.get(c)+"    ------>  ");
                    System.out.println(ProbData.get(x).Code.get(c));
                }
                    
                System.out.println("-------------------------------------");
            }
         }
            compression(Text, ProbData.get(0));
            WriteInFile(ProbData.get(0));
    }
             
    public static void main(String[] args) throws Exception{
//       String Text; 
//       System.out.println("Enter Your Data Please: ");
//       Scanner input = new Scanner(System.in);
//       Text= input.next();
//       StandardHuffman(Text);
//       Decompression();
              StandardHuffmanGUI k=new StandardHuffmanGUI();
              k.setVisible(true) ;
              
    }
}
