

public class QuickMain {
    public QuickMain() {
        super();
    }
    public String removeSpecialChars(String theData, boolean theEndLine)
    {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i < theData.length(); i++)
        {
            char ch =   theData.charAt(i) ;
            if (ch == '_' || ch == ',' || ch == '.' || ch == '/' || ch=='\\')
            {
                sb.append(ch);
            }
            else if ((Character.isLetterOrDigit(ch)) ||  (Character.isSpaceChar(ch)))
            {
                sb.append(ch);
            }
        }
        System.out.println("input => " + theData + " return => " + sb.toString());
        return sb.toString();
    }

    public void printLoop()
    {
        int [] [] arr = {{1,2,3}, {4,5,6},{7,8,9}, {3,2,1}};
        for (int j=0; j < arr.length; j++)
        {
            for (int k=j; k < arr[0].length; k++)
            {
                System.out.println(arr[j][k] + " ");
            }
            System.out.println();
        }
    }
    public static void main(String[] args)
    {
        System.out.println("begin main");
        QuickMain  quickMain = new QuickMain();
        String theData = "OHIO_VALLEY_TO_FLORIDA_1_PARTIAL";
        quickMain.removeSpecialChars( theData, true);
        System.out.println("Strat print loop");
        quickMain.printLoop();
        System.out.println("end main  ...that's all folks");
    }
}
