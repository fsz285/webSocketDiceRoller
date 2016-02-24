var stompClient;
var messagesClient;

function connectRolls() {
	var socket = new SockJS('/roll');
	rollsClient = Stomp.over(socket);
	rollsClient.connect({}, function(frame) {
		rollsClient.subscribe('/topic/rolls', function(dieRoll) {
			showRoll(JSON.parse(dieRoll.body).name,
					JSON.parse(dieRoll.body).numSides,
					JSON.parse(dieRoll.body).result,
					JSON.parse(dieRoll.body).privateRoll);
		});
	});
}

var color_codes = {};
function connectSettings() {
	var socket = new SockJS('/settings');
	settingsClient = Stomp.over(socket);
	try {
		settingsClient.connect({}, function(frame) {
			settingsClient.subscribe('/topic/settings', function(settings) {
				color_codes[JSON.parse(settings.body).name] = JSON
						.parse(settings.body).color;
				console.log(color_codes);
			});
		});
	} catch (e) {

	}

}

function connectMessages() {
	var socket = new SockJS('/message');
	messagesClient = Stomp.over(socket);
	try {
		messagesClient.connect({}, function(frame) {
			messagesClient.subscribe('/topic/messages', function(dieRoll) {
				showMessage(JSON.parse(dieRoll.body).name, JSON
						.parse(dieRoll.body).message);
			});
		});
	} catch (e) {

	}

}

$(document).ready(
		function() {
			connectRolls();
			connectMessages();
			connectSettings();
			$('#colorselector').colorselector({
				callback : function(value, color, title) {
					settingsClient.send("/app/settings", {}, JSON.stringify({
						'name' : makeName(name, charName),
						'color' : color
					}));
				}
			});
		});

function makeName(name, charName) {
	return charName + ' (' + name + ')'
}

function roll(numSides) {
	name = $("#name").val();
	charName = $("#char").val();
	isPrivate = $('#isPrivate').prop('checked');
	rollsClient.send("/app/roll", {}, JSON.stringify({
		'name' : makeName(name, charName),
		'numSides' : numSides,
		'privateRoll' : isPrivate
	}));
}

$(document).ready(function() {
	$("#message").keyup(function(e) {
		if (e.keyCode == 13) {
			talk();
		}
	});

});

function talk() {
	message = $("#message").val();
	name = $("#name").val();
	charName = $("#char").val();
	messagesClient.send("/app/message", {}, JSON.stringify({
		'name' : makeName(name, charName),
		'message' : message,
	}));
	$("#message").val("");
}

function stringToColorCode(str) {
	if (!(str in color_codes)) {
		settingsClient.send("/app/settings", {}, JSON.stringify({
			'name' : str
		}));
	}
	return color_codes[str];
}

function showMessage(name, message) {
	var response = document.getElementById('response');
	var dl;
	var lastRollerName;
	if ($(response).find("p").length > 0) {
		dl = $(response).find("p").last().find("dl").first();
		lastRollerName = dl.find("dt").first().text();
	}
	var dd = document.createElement('dd');

	dd.appendChild(document.createTextNode(message));
	dd.style.color = stringToColorCode(name);

	if (lastRollerName == name) {
		dl.append(dd);
	} else {
		var p = document.createElement('p');
		p.style.wordWrap = 'break-word';
		var dl = document.createElement('dl');
		dl.style.color = stringToColorCode(name);
		var dt = document.createElement('dt');
		dt.appendChild(document.createTextNode(name));
		dt.style.color = stringToColorCode(name);

		dl.appendChild(dt);
		dl.appendChild(dd);
		p.appendChild(dl);
		p.style.color = stringToColorCode(name);
		response.appendChild(p);
	}
	var rollContainer = document.getElementById('rollContainer');
	rollContainer.scrollTop = rollContainer.scrollHeight;
}

function showRoll(name, numSides, result, isPrivate) {
	var response = document.getElementById('response');
	var dl;
	var lastRollerName;
	if ($(response).find("p").length > 0) {
		dl = $(response).find("p").last().find("dl").first();
		lastRollerName = dl.find("dt").first().text();
	}
	var dd = document.createElement('dd');
	$(dd).css('font-style', 'italic');
	var myName = $('#name').val()
	var myCharName = $('#char').val()
	dd.style.color = stringToColorCode(makeName(myName, myCharName));
	if (!isPrivate) {
		dd.appendChild(document.createTextNode('d' + numSides + ' : ' + result));
	} else if (isPrivate && name == makeName(myName, myCharName)) {
		dd.appendChild(document.createTextNode('d' + numSides + ' : ' + result + ' (geheim)'));
	} else {
		dd.appendChild(document.createTextNode('geheimer Wurf'));
	}
	if (lastRollerName == name) {
		dd.style.color = stringToColorCode(name);
		dl.append(dd);
	} else {
		var p = document.createElement('p');
		p.style.wordWrap = 'break-word';
		var dl = document.createElement('dl');
		dl.style.color = stringToColorCode(name);
		var dt = document.createElement('dt');
		dt.style.color = stringToColorCode(name);
		dt.appendChild(document.createTextNode(name));

		dl.appendChild(dt);
		dl.appendChild(dd);
		p.appendChild(dl);
		p.style.color = stringToColorCode(name);
		response.appendChild(p);
	}
	var rollContainer = document.getElementById('rollContainer');
	rollContainer.scrollTop = rollContainer.scrollHeight;
}