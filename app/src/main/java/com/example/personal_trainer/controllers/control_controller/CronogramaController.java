package com.example.personal_trainer.controllers.control_controller;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personal_trainer.R;
import com.example.personal_trainer.models.control_model.CronogramaModel;
import com.example.personal_trainer.models.fitness_model.DetalleRutinaModel;
import com.example.personal_trainer.models.fitness_model.EjercicioModel;
import com.example.personal_trainer.models.fitness_model.RutinaModel;
import com.example.personal_trainer.models.registro_model.ClienteModel;
import com.example.personal_trainer.views.control_view.CronogramaView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CronogramaController extends AppCompatActivity {
    private CronogramaView cronogramaView;
    private CronogramaModel cronogramaModel;
    private ClienteModel clienteModel;
    private RutinaModel rutinaModel;
    private EjercicioModel ejercicioModel;

    private Calendar calendar;
    private int idCronogramaSeleccionado = -1;
    private int idClienteSeleccionado = -1;
    private int idRutinaSeleccionado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vcronograma);

        cronogramaModel = new CronogramaModel();
        cronogramaModel.initBD(this);
        clienteModel = new ClienteModel();
        clienteModel.initBD(this);
        rutinaModel = new RutinaModel();
        rutinaModel.initBD(this);
        ejercicioModel = new EjercicioModel();
        ejercicioModel.initBD(this);

        View rootView = findViewById(android.R.id.content);
        cronogramaView = new CronogramaView(this, rootView);

        calendar = Calendar.getInstance();

        setupListeners();

        cargarDatosIniciales();
    }

    private void setupListeners() {
        cronogramaView.setBtnInsertarListener(v -> manejarInsercionCronograma());
        cronogramaView.setBtnModificarListener(v -> manejarModificacionCronograma());
        cronogramaView.setBtnBorrarListener(v -> manejarBorradoCronograma());
        cronogramaView.setBtnEnviarRutinaListener(v -> manejarEnvioRutina());

        cronogramaView.setListViewCronogramasListener(this::manejarSeleccionCronograma);

        cronogramaView.setSpinnerClienteListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                actualizarClienteSeleccionado(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                idClienteSeleccionado = -1;
            }
        });

        cronogramaView.setSpinnerRutinaListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                actualizarRutinaSeleccionada(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                idRutinaSeleccionado = -1;
            }
        });

        cronogramaView.setFechaPickerListener(v -> mostrarSelectorFecha());
    }

    private void manejarInsercionCronograma() {
        String fecha = getFechaFormateadaParaBD();

        if (!validarDatosCronograma(fecha)) {
            return;
        }

        boolean exito = cronogramaModel.insertar(idClienteSeleccionado, idRutinaSeleccionado, fecha);

        if (exito) {
            cronogramaView.mostrarMensaje("Cronograma insertado correctamente");
            cargarDatosIniciales();
        } else {
            cronogramaView.mostrarMensaje("Error al insertar cronograma");
        }
    }

    private void manejarModificacionCronograma() {
        if (idCronogramaSeleccionado == -1) {
            cronogramaView.mostrarMensaje("No hay cronograma seleccionado");
            return;
        }

        String fecha = getFechaFormateadaParaBD();

        if (!validarDatosCronograma(fecha)) {
            return;
        }

        boolean exito = cronogramaModel.modificar(
                idCronogramaSeleccionado,
                idClienteSeleccionado,
                idRutinaSeleccionado,
                fecha
        );

        if (exito) {
            cronogramaView.mostrarMensaje("Cronograma modificado correctamente");
            cargarDatosIniciales();
        } else {
            cronogramaView.mostrarMensaje("Error al modificar cronograma");
        }
    }

    private void manejarBorradoCronograma() {
        if (idCronogramaSeleccionado == -1) {
            cronogramaView.mostrarMensaje("No hay cronograma seleccionado");
            return;
        }

        boolean exito = cronogramaModel.borrar(idCronogramaSeleccionado);

        if (exito) {
            cronogramaView.mostrarMensaje("Cronograma borrado correctamente");
            cargarDatosIniciales();
        } else {
            cronogramaView.mostrarMensaje("Error al borrar cronograma");
        }
    }

    private boolean validarDatosCronograma(String fecha) {
        if (idClienteSeleccionado == -1 || idRutinaSeleccionado == -1) {
            cronogramaView.mostrarMensaje("Debes seleccionar un cliente y una rutina");
            return false;
        }

        if (fecha.isEmpty()) {
            cronogramaView.mostrarMensaje("La fecha es obligatoria");
            return false;
        }

        if (cronogramaModel.existeCronograma(idClienteSeleccionado, fecha, idCronogramaSeleccionado)) {
            cronogramaView.mostrarMensaje("Este cliente ya tiene un cronograma para esta fecha");
            return false;
        }

        return true;
    }

    private void manejarSeleccionCronograma(AdapterView<?> parent, View view, int position, long id) {
        List<CronogramaModel> cronogramas = cronogramaModel.consultar();

        if (position < 0 || position >= cronogramas.size()) {
            resetearUI();
            return;
        }

        CronogramaModel cronograma = cronogramas.get(position);
        idCronogramaSeleccionado = cronograma.getIdCronograma();
        idClienteSeleccionado = cronograma.getIdCliente();
        idRutinaSeleccionado = cronograma.getIdRutina();

        mostrarDatosCronograma(cronograma);
        cronogramaView.habilitarModoEdicion(true);
        cronogramaView.activarBotonesEnvio(true);
    }

    private void mostrarDatosCronograma(CronogramaModel cronograma) {
        try {
            SimpleDateFormat sdfBD = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat sdfUI = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            calendar.setTime(sdfBD.parse(cronograma.getFecha()));
            cronogramaView.setFecha(sdfUI.format(calendar.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        seleccionarEnSpinners(cronograma.getIdCliente(), cronograma.getIdRutina());
    }

    private void seleccionarEnSpinners(int idCliente, int idRutina) {
        List<ClienteModel> clientes = clienteModel.consultar();
        List<RutinaModel> rutinas = rutinaModel.consultar();

        int posCliente = encontrarPosicionEnLista(clientes, idCliente);
        int posRutina = encontrarPosicionEnLista(rutinas, idRutina);

        cronogramaView.setPosicionClienteSeleccionado(posCliente + 1);
        cronogramaView.setPosicionRutinaSeleccionado(posRutina + 1);
    }

    private int encontrarPosicionEnLista(List<?> items, int id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof ClienteModel && ((ClienteModel)items.get(i)).getIdCliente() == id) {
                return i;
            }
            if (items.get(i) instanceof RutinaModel && ((RutinaModel)items.get(i)).getIdRutina() == id) {
                return i;
            }
        }
        return -1;
    }

    private void manejarEnvioRutina() {
        try {
            if (idRutinaSeleccionado == -1) {
                cronogramaView.mostrarMensaje("No hay rutina seleccionada");
                return;
            }

            if (idClienteSeleccionado == -1) {
                cronogramaView.mostrarMensaje("No hay cliente seleccionado");
                return;
            }

            ClienteModel cliente = clienteModel.buscar(idClienteSeleccionado);
            if (cliente == null || cliente.getCelular() == null || cliente.getCelular().isEmpty()) {
                cronogramaView.mostrarMensaje("El cliente no tiene número de teléfono registrado");
                return;
            }

            RutinaModel rutina = rutinaModel.buscar(idRutinaSeleccionado);
            if (rutina == null) {
                cronogramaView.mostrarMensaje("No se encontró la rutina seleccionada");
                return;
            }

            List<DetalleRutinaModel> detalles = rutina.getDetalleRutina();
            if (detalles == null || detalles.isEmpty()) {
                cronogramaView.mostrarMensaje("La rutina no tiene ejercicios asignados");
                return;
            }

            String mensajeRutina = "🏋️‍♂️ *Rutina: " + rutina.getNombre() + "* 🏋️‍♂️\n\n";
            mensajeRutina += "Aquí tienes los ejercicios de tu rutina:\n\n";
            Bitmap bitmapRutina = BitmapFactory.decodeResource(getResources(), R.drawable.ejecicio);

            cronogramaModel.shareImageWithText(this, bitmapRutina, mensajeRutina);

            for (DetalleRutinaModel detalle : detalles) {
                EjercicioModel ejercicio = ejercicioModel.buscar(detalle.getIdEjercicio());
                if (ejercicio != null) {
                    StringBuilder mensajeEjercicio = new StringBuilder();
                    mensajeEjercicio.append("💪 *Ejercicio: ").append(ejercicio.getNombre()).append("*\n\n");
                    mensajeEjercicio.append("Series: ").append(detalle.getCantidadSerie()).append("\n");
                    mensajeEjercicio.append("Repeticiones: ").append(detalle.getCantidadRepeticion()).append("\n");
                    mensajeEjercicio.append("Descanso: ").append(detalle.getDuracionReposo()).append(" segundos\n\n");

                    Bitmap bitmapEjercicio = obtenerImagenEjercicio(ejercicio);

                    if (bitmapEjercicio != null) {
                        cronogramaModel.shareImageWithText(this, bitmapEjercicio, mensajeEjercicio.toString());
                        Thread.sleep(500);
                    }
                }
            }

            cronogramaView.mostrarMensaje("Rutina enviada al cliente por WhatsApp");
        } catch (Exception e) {
            e.printStackTrace();
            cronogramaView.mostrarMensaje("Error al enviar la rutina: " + e.getMessage());
        }
    }

    private Bitmap obtenerImagenEjercicio(EjercicioModel ejercicio) {
        if (ejercicio.getUrlImagen() == null || ejercicio.getUrlImagen().isEmpty()) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.sin_imagen);
        }

        try {
            String imagePath = ejercicio.getUrlImagen();

            if (imagePath.startsWith("content")) {
                return obtenerBitmapDesdeUri(Uri.parse(imagePath));
            }
            else {
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return BitmapFactory.decodeResource(getResources(), R.drawable.sin_imagen);
    }

    private Bitmap obtenerBitmapDesdeUri(Uri uri) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), uri);
            return ImageDecoder.decodeBitmap(source);
        } else {
            return MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        }
    }

    private void cargarClientesEnSpinner() {
        List<String> nombresClientes = new ArrayList<>();
        nombresClientes.add("Elige una opción");

        for (ClienteModel cliente : clienteModel.consultar()) {
            nombresClientes.add(cliente.getNombre());
        }

        cronogramaView.configurarSpinnerClientes(nombresClientes);
    }

    private void cargarRutinasEnSpinner() {
        List<String> nombresRutinas = new ArrayList<>();
        nombresRutinas.add("Elige una opción");

        for (RutinaModel rutina : rutinaModel.consultar()) {
            nombresRutinas.add(rutina.getNombre());
        }

        cronogramaView.configurarSpinnerRutinas(nombresRutinas);
    }

    private void cargarCronogramas() {
        List<String> cronogramasTexto = new ArrayList<>();
        List<CronogramaModel> cronogramas = cronogramaModel.consultar();

        for (CronogramaModel cronograma : cronogramas) {
            ClienteModel cliente = clienteModel.buscar(cronograma.getIdCliente());
            RutinaModel rutina = rutinaModel.buscar(cronograma.getIdRutina());

            String texto = String.format("%s - %s - %s",
                    cronograma.getFecha(),
                    cliente != null ? cliente.getNombre() : "?",
                    rutina != null ? rutina.getNombre() : "?");

            cronogramasTexto.add(texto);
        }

        cronogramaView.mostrarCronogramasEnListView(cronogramasTexto);
    }

    private void actualizarClienteSeleccionado(int position) {
        List<ClienteModel> clientes = clienteModel.consultar();
        idClienteSeleccionado = position > 0 ? clientes.get(position - 1).getIdCliente() : -1;
    }

    private void actualizarRutinaSeleccionada(int position) {
        List<RutinaModel> rutinas = rutinaModel.consultar();
        idRutinaSeleccionado = position > 0 ? rutinas.get(position - 1).getIdRutina() : -1;
    }

    private void mostrarSelectorFecha() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this::onDateSet,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void onDateSet(DatePicker view, int year, int month, int day) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        actualizarFechaEnVista();
    }

    private void actualizarFechaEnVista() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        cronogramaView.setFecha(sdf.format(calendar.getTime()));
    }

    private String getFechaFormateadaParaBD() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    private void resetearUI() {
        cronogramaView.limpiarCampos();
        idCronogramaSeleccionado = -1;
        idClienteSeleccionado = -1;
        idRutinaSeleccionado = -1;
    }

    private void cargarDatosIniciales() {
        cargarClientesEnSpinner();
        cargarRutinasEnSpinner();
        cargarCronogramas();
        resetearUI();
    }
}