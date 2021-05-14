package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;

public class Validador {

  public void validarPoner(double cuanto, Cuenta cuenta) {
    this.validarMontoIngresado(cuanto);

    this.validarDeposito(cuenta);
  }

  public void validarSacar(double cuanto, Cuenta cuenta) {
    this.validarMontoIngresado(cuanto);

    this.validarExtraccion(cuanto, cuenta);
  }

  public void validarMontoIngresado(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public void validarDeposito(Cuenta cuenta) {
    if (cuenta.getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }

  public void validarExtraccion(double cuanto, Cuenta cuenta) {
    this.validarSaldoDisponible(cuanto, cuenta);
    this.ValidarExtraccionesDiarias(cuanto, cuenta);
  }

  private void ValidarExtraccionesDiarias(double cuanto, Cuenta cuenta) {
    double montoExtraidoHoy = cuenta.getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
  }

  private void validarSaldoDisponible(double cuanto, Cuenta cuenta) {
    if (cuenta.getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + cuenta.getSaldo() + " $");
    }
  }
}
