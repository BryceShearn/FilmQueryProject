package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	private static final String USER = "student";
	private static final String PASS = "student";

	@Override
	public Film findFilmById(int filmId) {
		Film film = null;

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);) {
			String sql = "SELECT film.id, film.title, film.description, film.release_year,"
					   		  + "lang.name, film.rental_duration, film.length, film.rental_rate,"
					   		  + "film.replacement_cost, film.rating, film.special_features "
					   + "FROM film JOIN language lang "
					   + "ON film.language_id = lang.id "
					   + "WHERE film.id = ?"
					   ;
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet filmResult = stmt.executeQuery();
			if (filmResult.next()) {
				film = new Film();
				film.setId(filmResult.getInt("film.id"));
				film.setTitle(filmResult.getString("film.title"));
				film.setDescription(filmResult.getString("film.description"));
				film.setReleaseYear(filmResult.getInt("film.release_year"));
				film.setLanguage(filmResult.getString("lang.name"));
				film.setRentalDuration(filmResult.getInt("film.rental_duration"));
				film.setLength(filmResult.getInt("film.length"));
				film.setRentalRate(filmResult.getDouble("film.rental_rate"));
				film.setReplacementCost(filmResult.getDouble("film.replacement_cost"));
				film.setRating(filmResult.getString("film.rating"));
				film.setSpecialFeatures(filmResult.getString("film.special_features"));
				film.setActors(findActorsByFilmId(film.getId()));
			}
			filmResult.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
	}
	@Override
	public List<Film> findFilmByUserKeyword(String userKeyword) {
		List<Film> films = new ArrayList<>();

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);) {
			String sql = "SELECT film.id, film.title, film.description, film.release_year, lang.name,"
					   		  + "film.rental_duration, film.length, film.rental_rate, film.replacement_cost,"
					   		  + "film.rating, film.special_features "
					   + "FROM film JOIN language lang "
					   + "ON film.language_id = lang.id "
					   + "WHERE film.title LIKE ? "
					   + "OR film.description LIKE ? "
					   ;
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%" + userKeyword + "%");
			stmt.setString(2, "%" + userKeyword + "%");
			ResultSet filmResult = stmt.executeQuery();
			while (filmResult.next()) {
				Film film = new Film();

				film.setId(filmResult.getInt("film.id"));
				film.setTitle(filmResult.getString("film.title"));
				film.setDescription(filmResult.getString("film.description"));
				film.setReleaseYear(filmResult.getInt("film.release_year"));
				film.setLanguage(filmResult.getString("lang.name"));
				film.setRentalDuration(filmResult.getInt("film.rental_duration"));
				film.setLength(filmResult.getInt("film.length"));
				film.setRentalRate(filmResult.getDouble("film.rental_rate"));
				film.setReplacementCost(filmResult.getDouble("film.replacement_cost"));
				film.setRating(filmResult.getString("film.rating"));
				film.setSpecialFeatures(filmResult.getString("film.special_features"));
				film.setActors(findActorsByFilmId(film.getId()));
				films.add(film);
			}
			filmResult.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return films;
	}

	@Override
	public Actor findActorById(int actorId) {
		Actor actor = null;

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);) {
			String sql = "SELECT id, first_name, last_name "
					   + "FROM actor "
					   + "WHERE id = ? "
					   ;
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet actorResult = stmt.executeQuery();
			if (actorResult.next()) {
				actor = new Actor();
				actor.setId(actorResult.getInt("id"));
				actor.setFirstName(actorResult.getString("first_name"));
				actor.setLastName(actorResult.getString("last_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actor;
	}
	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actors = new ArrayList<>();
		Actor actor = null;

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);) {
			String sql = "SELECT actor.id, actor.first_name, actor.last_name "
					   + "FROM actor "
					   + "JOIN film_actor ON actor.id = film_actor.actor_id " 
					   + "JOIN film ON film_actor.film_id = film.id "
					   + "WHERE film.id = ? "
					   ;
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet actorResult = stmt.executeQuery();
			while (actorResult.next()) {
				actor = new Actor();
				actor.setId(actorResult.getInt("actor.id"));
				actor.setFirstName(actorResult.getString("actor.first_name"));
				actor.setLastName(actorResult.getString("actor.last_name"));
				actors.add(actor);
		}
		actorResult.close();
		stmt.close();
		conn.close();
	} catch (SQLException e) {
		e.printStackTrace();
	}
		return actors;
	}
}