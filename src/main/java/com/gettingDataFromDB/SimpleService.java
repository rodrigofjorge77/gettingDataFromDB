/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.gettingDataFromDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SimpleService {
	
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Dados de conexão (substitua pelos seus)
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "Admin";

        // Nome da tabela e consulta SQL
        String tableName = "dev.orders";
        String query = "SELECT * FROM " + tableName;

        try {
            // Carrega o driver PostgreSQL
            Class.forName("org.postgresql.Driver");

            // Cria a conexão
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();

            // Executa a consulta
            ResultSet rs = stmt.executeQuery(query);

            // Cria uma lista para armazenar os resultados
            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

            // Mapeia os resultados do ResultSet para um Map
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<String, Object>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(rsmd.getColumnName(i), rs.getObject(i));
                }
                data.add(row);
            }

            // Fecha a conexão
            rs.close();
            stmt.close();
            conn.close();

            // Converte a lista para JSON
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(data);

            // Grava o JSON em um arquivo
            FileWriter file = new FileWriter("C:\\tmp\\dados.json");
            file.write(jsonString);
            file.close();

            System.out.println("Dados exportados para dados.json com sucesso!");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
