package app;

import db.DBInitializer;
import model.Patient;
import model.Visit;
import model.exceptions.DBAccessException;
import model.exceptions.ValidationException;
import service.PatientService;
import service.VisitService;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        DBInitializer.initializeDB();

        PatientService patientService = new PatientService();
        VisitService visitService = new VisitService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n==== MENU ====");
            System.out.println("1. Insert patient");
            System.out.println("2. Show all patients");
            System.out.println("3. Search patient by name");
            System.out.println("4. Update patient");
            System.out.println("5. Delete patient");
            System.out.println("6. Insert visit");
            System.out.println("7. Show visits for patient");
            System.out.println("8. Update visit");
            System.out.println("9. Delete visit");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1" -> insertPatientFlow(patientService, scanner);
                    case "2" -> printPatients(patientService.getAllPatients());
                    case "3" -> searchPatientFlow(patientService, scanner);
                    case "4" -> updatePatientFlow(patientService, scanner);
                    case "5" -> deletePatientFlow(patientService, scanner);
                    case "6" -> insertVisitFlow(visitService, scanner);
                    case "7" -> showPatientVisitsFlow(visitService, scanner);
                    case "8" -> updateVisitFlow(visitService, scanner);
                    case "9" -> deleteVisitFlow(visitService, scanner);
                    case "0" -> {
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (ValidationException e) {
                System.out.println("Validation error: " + e.getMessage());
            } catch (DBAccessException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric input.");
            }
        }
    }

    private static void insertPatientFlow(PatientService service, Scanner scanner)
            throws ValidationException, DBAccessException {

        System.out.print("First name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        System.out.print("AMKA (11 digits): ");
        String amka = scanner.nextLine();

        service.insertPatient(firstName, lastName, phone, amka);
        System.out.println("Patient inserted successfully.");
    }

    private static void searchPatientFlow(PatientService service, Scanner scanner)
            throws DBAccessException {

        System.out.print("Search input: ");
        String search = scanner.nextLine();

        List<Patient> results = service.searchPatientsByName(search);
        printPatients(results);
    }

    private static void updatePatientFlow(PatientService service, Scanner scanner)
            throws ValidationException, DBAccessException {

        System.out.print("Patient ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("New first name: ");
        String firstName = scanner.nextLine();

        System.out.print("New last name: ");
        String lastName = scanner.nextLine();

        System.out.print("New phone: ");
        String phone = scanner.nextLine();

        System.out.print("New AMKA (11 digits): ");
        String amka = scanner.nextLine();

        service.updatePatient(firstName, lastName, phone, amka, id);
        System.out.println("Patient updated successfully.");
    }

    private static void deletePatientFlow(PatientService service, Scanner scanner)
            throws DBAccessException {

        System.out.print("Patient ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        service.deletePatient(id);
        System.out.println("Patient deleted successfully.");
    }

    private static void insertVisitFlow(VisitService service, Scanner scanner)
            throws ValidationException, DBAccessException {

        System.out.print("Patient ID: ");
        int patientId = Integer.parseInt(scanner.nextLine());

        System.out.print("Visit notes: ");
        String notes = scanner.nextLine();

        System.out.print("Paid? (true/false): ");
        boolean paid = Boolean.parseBoolean(scanner.nextLine());

        System.out.print("Day: ");
        int day = Integer.parseInt(scanner.nextLine());

        System.out.print("Month: ");
        int month = Integer.parseInt(scanner.nextLine());

        System.out.print("Year: ");
        int year = Integer.parseInt(scanner.nextLine());

        service.insertVisit(notes, paid, day, month, year, patientId);
        System.out.println("Visit inserted successfully.");
    }

    private static void showPatientVisitsFlow(VisitService service, Scanner scanner)
            throws DBAccessException {

        System.out.print("Patient ID: ");
        int patientId = Integer.parseInt(scanner.nextLine());

        List<Visit> visits = service.getPatientVisits(patientId);
        printVisits(visits);
    }

    private static void updateVisitFlow(VisitService service, Scanner scanner)
            throws ValidationException, DBAccessException {

        System.out.print("Visit ID to update: ");
        int visitId = Integer.parseInt(scanner.nextLine());

        System.out.print("Patient ID: ");
        int patientId = Integer.parseInt(scanner.nextLine());

        System.out.print("New visit notes: ");
        String notes = scanner.nextLine();

        System.out.print("Paid? (true/false): ");
        boolean paid = Boolean.parseBoolean(scanner.nextLine());

        System.out.print("Day: ");
        int day = Integer.parseInt(scanner.nextLine());

        System.out.print("Month: ");
        int month = Integer.parseInt(scanner.nextLine());

        System.out.print("Year: ");
        int year = Integer.parseInt(scanner.nextLine());

        service.updateVisit(notes, paid, day, month, year, patientId, visitId);
        System.out.println("Visit updated successfully.");
    }

    private static void deleteVisitFlow(VisitService service, Scanner scanner)
            throws DBAccessException {

        System.out.print("Visit ID to delete: ");
        int visitId = Integer.parseInt(scanner.nextLine());

        service.deleteVisit(visitId);
        System.out.println("Visit deleted successfully.");
    }

    private static void printPatients(List<Patient> patients) {
        if (patients == null || patients.isEmpty()) {
            System.out.println("No patients found.");
            return;
        }

        for (Patient p : patients) {
            System.out.println(
                    "ID: " + p.getPatientId() +
                            ", First Name: " + p.getPatientFirstName() +
                            ", Last Name: " + p.getPatientLastName() +
                            ", Phone: " + p.getPatientPhone() +
                            ", AMKA: " + p.getPatientAmka()
            );
        }
    }

    private static void printVisits(List<Visit> visits) {
        if (visits == null || visits.isEmpty()) {
            System.out.println("No visits found.");
            return;
        }

        for (Visit v : visits) {
            System.out.println(
                    "Visit ID: " + v.getId() +
                            ", Notes: " + v.getVisitNotes() +
                            ", Paid: " + v.isPaidVisit() +
                            ", Date: " + v.getVisitDate() +
                            ", Patient ID: " + v.getPatientid()
            );
        }
    }
}