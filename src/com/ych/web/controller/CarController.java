package com.ych.web.controller;

import java.util.List;

import com.ych.common.BaseController;
import com.ych.core.plugin.annotation.Control;
import com.ych.web.model.CarModel;

@Control(controllerKey = "/car")
public class CarController extends BaseController {

	public void list() {
		Integer tID = getParaToInt("type");
		List<CarModel> cars = CarModel.dao.getCars(tID);
		renderJson(cars);
	}

}
