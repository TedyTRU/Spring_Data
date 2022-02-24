import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Engine implements Runnable {

    private final EntityManager entityManager;
    private BufferedReader bufferedReader;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        System.out.println("Enter exercise number: ");

        try {
            int exNum = Integer.parseInt(bufferedReader.readLine());

            switch (exNum) {
                case 2: exercise2ChangeCasing();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exercise2ChangeCasing() {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("UPDATE Town t " +
                "SET t.name = UPPER(t.name) " +
                "WHERE LENGTH(t.name) < 5 ");

        int affectedRows = query.executeUpdate();
        System.out.println(affectedRows);

        entityManager.getTransaction().commit();
    }
}
