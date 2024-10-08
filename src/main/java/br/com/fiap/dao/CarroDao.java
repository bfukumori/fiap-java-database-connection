package br.com.fiap.dao;

import br.com.fiap.exception.NotFoundException;
import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.model.CarroModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarroDao {
    private final Connection connection;

    public CarroDao() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }

    public void closeConnection() throws SQLException {
        this.connection.close();
    }

    public void create(CarroModel carroModel) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO CARROS (id, placa, modelo, fabricante, cor) VALUES ( SEQ_CARROS.nextval, ?, ?, ?, ?)");

        preparedStatement.setString(1, carroModel.getPlaca());
        preparedStatement.setString(2, carroModel.getModelo());
        preparedStatement.setString(3, carroModel.getFabricante());
        preparedStatement.setString(4, carroModel.getCor());

        preparedStatement.executeUpdate();
    }

    public List<CarroModel> list() throws SQLException {
        List<CarroModel> carroModels = new ArrayList<CarroModel>();

        Statement statement = this.connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM CARROS");

        while (resultSet.next()) {
            CarroModel carroModel = new CarroModel(resultSet.getString("placa"), resultSet.getString("modelo"), resultSet.getString("fabricante"), resultSet.getString("cor"));

            carroModels.add(carroModel);
        }

        return carroModels;
    }

    public void delete(String placa) throws SQLException, NotFoundException {
        PreparedStatement stm = this.connection.prepareStatement("DELETE FROM CARROS WHERE placa = ?");
        stm.setString(1, placa);
        int line = stm.executeUpdate();

        if (line == 0) {
            throw new NotFoundException("Carro não encontrado para ser deletado!");
        }

    }

    public void update(CarroModel carroModel) throws SQLException, NotFoundException {
        PreparedStatement stm = this.connection.prepareStatement("UPDATE CARROS SET modelo = ?,fabricante = ?,cor = ?  WHERE placa = ?");
        stm.setString(1, carroModel.getModelo());
        stm.setString(2, carroModel.getFabricante());
        stm.setString(3, carroModel.getCor());
        int line = stm.executeUpdate();

        if (line == 0) {
            throw new NotFoundException("Carro não encontrado para ser deletado!");
        }
    }
}
