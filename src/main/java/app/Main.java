package app;

import db.DBInitializer;
import model.Patient;
import model.exceptions.DBAccessException;
import model.exceptions.InvalidPhoneException;
import model.exceptions.ValidationException;
import service.PatientService;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        DBInitializer.initializeDB();
        PatientService service = new PatientService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n==== PATIENT MENU ====");
            System.out.println("1. Insert patient");
            System.out.println("2. Show all patients");
            System.out.println("3. Search patient by name");
            System.out.println("4. Update patient");
            System.out.println("5. Delete patient");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1" -> insertPatientFlow(service, scanner);
                    case "2" -> printPatients(service.getAllPatients());
                    case "3" -> searchPatientFlow(service, scanner);
                    case "4" -> updatePatientFlow(service, scanner);
                    case "5" -> deletePatientFlow(service, scanner);
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
        System.out.println(phone);

        System.out.print("AMKA (10 digits): ");
        String amka = scanner.nextLine();
        if(!amka.matches("\\d{11}")){
            System.out.println(amka);
        }

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

        System.out.print("New AMKA (10 digits): ");
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
}