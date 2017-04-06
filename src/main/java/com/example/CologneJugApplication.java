package com.example;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import com.vaadin.data.Binder;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ProgressBarRenderer;
import com.vaadin.ui.themes.ValoTheme;

@SpringBootApplication
public class CologneJugApplication {

	public static void main(String[] args) {
		SpringApplication.run(CologneJugApplication.class, args);
	}
}

@Component
class DataGenerator implements CommandLineRunner {

	@Autowired
	BeverageRepository repo;

	@Override
	public void run(String... arg0) throws Exception {
		Stream.of("Beer", "Cider").map(str -> new Beverage(str, 5.5d)).forEach(repo::save);
	}

}

@SpringUI
class MyUI extends UI {

	@Autowired
	BeverageRepository repo;

	@Override
	protected void init(VaadinRequest request) {
		Grid<Beverage> grid = new Grid<>();
		grid.addColumn(Beverage::getBeverageName).setCaption("Beverage");
		grid.addColumn(beverage -> beverage.getAlcoholContent() / 100, new ProgressBarRenderer()).setCaption("Alc %");
		grid.setItems(repo.findAll());
		grid.setSizeFull();

		VerticalLayout formLayout = new VerticalLayout();
		formLayout.setWidth("400px");
		HorizontalLayout horizontalLayout = new HorizontalLayout(formLayout, grid);
		horizontalLayout.setSizeFull();
		horizontalLayout.setExpandRatio(grid, 1.0f);

		TextField name = new TextField("Beverage Name");
		Slider alcoholSlider = new Slider("Alcohol content");
		alcoholSlider.setMin(0.5d);
		alcoholSlider.setMax(50.0d);
		alcoholSlider.setResolution(1);

		name.setSizeFull();
		alcoholSlider.setSizeFull();

		Binder<Beverage> binder = new Binder<>();
		binder.forField(name).asRequired("You need to provide a name").bind(Beverage::getBeverageName,
				Beverage::setBeverageName);
		binder.forField(alcoholSlider).bind(Beverage::getAlcoholContent, Beverage::setAlcoholContent);

		Button button = new Button("Create", e -> {
			Beverage beverage = new Beverage();
			if (binder.writeBeanIfValid(beverage)) {
				repo.save(beverage);
				grid.setItems(repo.findAll());
			} else {
				Notification.show("Invalid!");
			}
		});
		button.setStyleName(ValoTheme.BUTTON_PRIMARY);
		formLayout.addComponents(name, alcoholSlider, button);

		grid.addSelectionListener(e -> {
			e.getFirstSelectedItem().ifPresent(binder::readBean);
		});

		setContent(horizontalLayout);
	}

}

@RepositoryRestResource(path="beverage")
interface BeverageRepository extends JpaRepository<Beverage, Long> {
}
