package edu.ib.service;

import edu.ib.object.appointment.AllAppointmentView;
import edu.ib.object.appointment.Appointment;
import edu.ib.object.appointment.FreeAppointmentView;
import edu.ib.object.doctor.DoctorDto;
import edu.ib.object.employee.EmployeeDto;
import edu.ib.otherModels.Timetable;
import edu.ib.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class AppointmentService {

    private AppointmentRepository appointmentRepository;
    private DoctorDtoRepository doctorDtoRepository;
    private EmployeeDtoRepository employeeDtoRepository;
    private FreeAppointmentViewRepository freeAppointmentViewRepository;
    private AllAppointmentsViewRepository allAppointmentsViewRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, DoctorDtoRepository doctorDtoRepository, EmployeeDtoRepository employeeDtoRepository, FreeAppointmentViewRepository freeAppointmentViewRepository, AllAppointmentsViewRepository allAppointmentsViewRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorDtoRepository = doctorDtoRepository;
        this.employeeDtoRepository = employeeDtoRepository;
        this.freeAppointmentViewRepository = freeAppointmentViewRepository;
        this.allAppointmentsViewRepository = allAppointmentsViewRepository;
    }

    public void addAppointmentsFromTimetable(Timetable timetable, Long employeePesel) throws Exception {
        LocalDate dateCounter=timetable.getStartDateAsDate();
        LocalDate endDate=timetable.getEndDateAsDate();
        LocalTime endTime=timetable.getEndTimeAsTime();
        Optional<DoctorDto> doctor=doctorDtoRepository.findById(timetable.getDoctorPesel());
        Optional<EmployeeDto> employee=employeeDtoRepository.findById(employeePesel);
        if(doctor.isEmpty()) throw new Exception("No doctor in database with pesel: "+timetable.getDoctorPesel());
        if(employee.isEmpty()) throw new Exception("No employee in database with pesel: "+ employeePesel);
        while(dateCounter.isBefore(endDate.plusDays(1))){
            boolean correctDateOfWeek=false;
            switch(dateCounter.getDayOfWeek().getValue()){
                case 1:
                    correctDateOfWeek=timetable.isMonday();
                    break;
                case 2:
                    correctDateOfWeek=timetable.isTuesday();
                    break;
                case 3:
                    correctDateOfWeek=timetable.isWednesday();
                    break;
                case 4:
                    correctDateOfWeek=timetable.isThursday();
                    break;
                case 5:
                    correctDateOfWeek=timetable.isFriday();
                    break;
                case 6:
                    correctDateOfWeek=timetable.isSaturday();
                    break;
                case 7:
                    correctDateOfWeek=timetable.isSunday();
                    break;
            }
            if(correctDateOfWeek) {
                LocalTime counterTime = timetable.getStartTimeAsTime();
                while (counterTime.isBefore(endTime)) {
                    Appointment appointment = new Appointment();

                    appointment.setDoctor(doctor.get());
                    LocalDateTime dateTime = LocalDateTime.of(dateCounter, counterTime);
                    appointment.setDateTime(dateTime);
                    appointment.setEmployee(employee.get());
                    appointment.setPatient(null);
                    appointmentRepository.save(appointment);
                    counterTime = counterTime.plusMinutes(timetable.getTimeBetween());
                }
            }
            dateCounter=dateCounter.plusDays(1);
        }
    }

    public void addAppointment(Appointment appointment){
        this.appointmentRepository.save(appointment);
    }

    public Iterable<FreeAppointmentView> getFreeViewAppointments(){
        return freeAppointmentViewRepository.findAll();
    }

    public Iterable<AllAppointmentView> getAllViewAppointments(){
        return allAppointmentsViewRepository.findAll();

    }
    public void deleteAppointment(Long id){
        appointmentRepository.deleteById(id);
    }
}
