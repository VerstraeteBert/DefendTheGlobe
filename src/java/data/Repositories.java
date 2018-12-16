package data;

public class Repositories {

    private static BreakoutRepository mysqlBreakoutRepository = new BreakoutRepositoryMySql();

    public static BreakoutRepository getMysqlBreakoutRepository() {
        return mysqlBreakoutRepository;
    }
}