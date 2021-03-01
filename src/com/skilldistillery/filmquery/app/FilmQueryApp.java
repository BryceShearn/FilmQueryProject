package com.skilldistillery.filmquery.app;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {
		private DatabaseAccessor dao = new DatabaseAccessorObject();

		public static void main(String[] args) {
			FilmQueryApp app = new FilmQueryApp();
			app.launch();
		  //app.test();
		}
		private void launch() {
			Scanner sc = new Scanner(System.in);
			startUserInterface(sc);
			sc.close();
		}
		private void startUserInterface(Scanner sc) {
			System.out.println("*------------------------------------------------------------------*");
			System.out.println("|Welcome to the Film Query App! Please select an option from below!|");
			System.out.println("|1. Look up a film by its id.                                      |");
			System.out.println("|2. Look up a film by a search keyword.                            |");
			System.out.println("|3. Exit the application.                                          |");
			System.out.println("*------------------------------------------------------------------*");
			int choice = 0;
			try {
				choice = sc.nextInt();
			} 
			catch (InputMismatchException e) {
			} 
			finally {
				sc.nextLine();
			}

			switch (choice) {
			case 1:
				beginFindFilmByID(sc);
				break;
			case 2:
				beginUserEnteredKeywordSearch(sc);
				break;
			case 3:
				System.out.println();
				System.out.println("Thanks for using the Film Query App, see you soon!");
				System.exit(0);
				break;
			default:
				System.err.println("Invalid Input, Please try again!");
				startUserInterface(sc);
				break;
			}
		}
		private void beginFindFilmByID(Scanner sc) {
			System.out.print("Please enter a Film ID: ");
			int filmID = 0;
			try {
				filmID = sc.nextInt();
			} 
			catch (InputMismatchException e) {
				sc.nextLine();
				System.err.println("Invalid Input, Please try again!");
				System.out.println();
				startUserInterface(sc);
			} 
			finally {
				sc.nextLine();
			}
			Film film = dao.findFilmById(filmID);
				if (film == null) {
				System.out.println("no luck :/ Try another search and give us some time to find that movie!");
				} 
				else {
				System.out.println(film.getTitle());
				System.out.println(film.getReleaseYear());
				System.out.println(film.getRating());
				System.out.println(film.getDescription());			
				System.out.println(film.getLanguage());
				System.out.println(film.getActors().toString());
				}
			System.out.println();
			startUserInterface(sc);
		}
		
		private void beginUserEnteredKeywordSearch(Scanner sc) {
			System.out.print("Please enter a keyword and we'll have a look around!:");
			String userInputKeyword = sc.nextLine();
			System.out.println();

			List<Film> films = dao.findFilmByUserKeyword(userInputKeyword);
			if (films.size() == 0) {
				System.out.println("Hmm.. we dont have anything on that keyword. Can you think of another?");
			} 
			else {
				for (Film userKeywordFilms: films) {
					System.out.println(userKeywordFilms.getTitle());
					System.out.println(userKeywordFilms.getReleaseYear());
					System.out.println(userKeywordFilms.getRating());
					System.out.println(userKeywordFilms.getDescription());
					System.out.println(userKeywordFilms.getLanguage());
					System.out.println(userKeywordFilms.getActors().toString());
				}
				System.out.println();
			}
			System.out.println();
			startUserInterface(sc);
		}
}