package com.eatfull.buyerorder.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Service
public class TruncateDatabaseService {
    @Autowired
    protected DataSource dataSource;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void restartIdWith(int startId, boolean truncate, List<String> tables) throws Exception {
        List<String> tableNames = new ArrayList<>(tables == null ? emptyList() : tables);
        if (isEmpty(tableNames)) {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            ResultSet tableList = metaData.getTables(null, null, null, new String[]{"TABLE"});
            while (tableList.next()) {
                String tableName = tableList.getString("TABLE_NAME");
                tableNames.add(tableName);
            }
        }

        tableNames.remove("labour_rate");

        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        tableNames.stream()
                .filter(tableName -> !tableName.endsWith("_view"))
                .collect(Collectors.toList())
                .forEach(
                        tableName -> {
                            if (truncate) {
                                entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
                            }
                            entityManager.createNativeQuery("ALTER TABLE " + tableName
                                    + " AUTO_INCREMENT = " + startId).executeUpdate();
                        }
                );
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }
}
