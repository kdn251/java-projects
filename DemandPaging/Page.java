
/**
 * Created by kdn251 on 11/30/16.
 */
public class Page {

    protected int pageNumber;
    protected int processNumber;
    protected int loadTime;

    public Page() {

        //default constructor

    }

    public Page(int pageNumber, int processNumber) {

        this.pageNumber = pageNumber;
        this.processNumber = processNumber;

    }

    @Override
    public boolean equals(Object object) {

        //cast object to page object
        Page otherPage = (Page)object;

        //check if the current page and other page are equal
        if(this.processNumber == otherPage.processNumber && this.pageNumber == otherPage.pageNumber) {

            return true;

        }

        //current page and other page are not equal
        return false;

    }
}
