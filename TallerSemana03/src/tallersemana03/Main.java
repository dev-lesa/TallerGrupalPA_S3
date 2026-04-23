package tallersemana03;

import Clases.Celulares;
import Clases.Chips;
import Clases.Recargas;
import Clases.Usuarios;
import Logica.CelularesJpaController;
import Logica.ChipsJpaController;
import Logica.RecargasJpaController;
import Logica.UsuariosJpaController;
import java.math.BigDecimal;
import java.util.Date;

public class Main {

    public static void main(String[] args) {

        UsuariosJpaController ctrlUsuario = new UsuariosJpaController();
        CelularesJpaController ctrlCelular = new CelularesJpaController();
        ChipsJpaController ctrlChip = new ChipsJpaController();
        RecargasJpaController ctrlRecarga = new RecargasJpaController();

        Usuarios objUsuario = new Usuarios();
        objUsuario.setCedula("1105405137");
        objUsuario.setNombre("Luis");
        objUsuario.setApellido("Sanchez");
        ctrlUsuario.create(objUsuario);

        Celulares objCelular = new Celulares();
        objCelular.setMarca("iphone");
        objCelular.setModelo("13 pro max");
        objCelular.setIdUsuarios(objUsuario);
        ctrlCelular.create(objCelular);

        Chips objChip = new Chips();
        objChip.setOperadora("tuenti");
        objChip.setChipSlot(1);
        objChip.setNumero("0991234568");
        objChip.setSaldo(new BigDecimal("0.00"));
        objChip.setIdCelulares(objCelular);
        try {
            ctrlChip.create(objChip);
        } catch (Exception e) {
            System.out.println("Error al insertar chip: " + e.getMessage());
        }

        Recargas objRecarga = new Recargas();
        objRecarga.setValor(new BigDecimal("2.50"));
        objRecarga.setFechaRecarga(new Date());
        objRecarga.setIdChips(objChip);
        ctrlRecarga.create(objRecarga);

        Chips objChipDuplicado = new Chips();
        objChipDuplicado.setOperadora("Movistar");
        objChipDuplicado.setChipSlot(1);
        objChipDuplicado.setNumero("0963344090");
        objChipDuplicado.setSaldo(new BigDecimal("1.00"));
        objChipDuplicado.setIdCelulares(objCelular);
        try {
            ctrlChip.create(objChipDuplicado);
        } catch (Exception e) {
            System.out.println("Validacion correcta: " + e.getMessage());
        }
    }
}
