requireAuth(); //require auth for access

// const API_URL = "http://localhost:8080";
const API_URL = "https://vehiclemaintenanceplatform.onrender.com";

/* constant variables */
const loggedUser = JSON.parse( localStorage.getItem("loggedUser") );
document.getElementById("usernameLabel").textContent = `👤 ${loggedUser.username}`;

let cars = [];
let selectedCarId = null;

/* ===================== SERVICE TYPES ===================== */
const SERVICE_TYPES = {
    oil: ["масло двигател", "engine oil", "oil change"],
    gearboxOil: ["масло скоростна кутия","gearbox oil","transmission oil","gear oil"],
    oilFilter: ["маслен филтър", "oil filter"],
    airFilter: ["въздушен филтър", "air filter"],
    fuelFilter: ["горивен филтър", "fuel filter"],
    cabinFilter: ["филтър купе", "купе филтър", "cabin filter", "pollen filter"],
    brakeDiscs: ["спирачни дискове", "brake disc", "brake disk"],
    brakePads: ["спирачни накладки", "brake pad"]
};

function logout() {
    localStorage.removeItem("loggedUser");
    window.location.href = "index.html";
}

/* ===================== HELPERS ===================== */
function normalize(text) {
    return text
        .toLowerCase()
        .replace(/,/g, " ")
        .replace(/\s+/g, " ")
        .trim();
}

function detectServices(description) {
    const normalized = normalize(description);
    const detected = [];

    for (const [type, keywords] of Object.entries(SERVICE_TYPES)) {
        if (keywords.some(k => normalized.includes(k))) {
            detected.push(type);
        }
    }
    return detected;
}

/* ===================== NAVIGATION ===================== */
function toggleMenu() {
    document.getElementById("sideMenu").classList.toggle("open");
}

function showSection(id) {
    document.querySelectorAll(".page").forEach(p => p.classList.add("hidden"));
    document.getElementById(id).classList.remove("hidden");
    toggleMenu();
}

function toggleCustomDescription() {
    const input = document.getElementById("customDescription");
    input.disabled = !document.getElementById("customDescCheckbox").checked;

    if (!input.disabled) {
        input.focus();
    } else {
        input.value = "";
    }
}

function buildDescription() {
    const selected = [];

    document.querySelectorAll(".service-checkbox:checked").forEach(cb => {
        selected.push(cb.value);
    });

    const customEnabled = document.getElementById("customDescCheckbox").checked;
    const customText = document.getElementById("customDescription").value.trim();

    if (customEnabled && customText) {
        selected.push(customText);
    }

    return selected.join(", ");
}

/* ===================== ON LOAD ===================== */
window.onload = () => {
    loadCars();
    renderQuickCards({});
    loadMaintenanceForSelectedCar();
};

/* ===================== ADD ===================== */
function addCar() {
    const errorMsg = document.getElementById("addCarErrorMsg");
    errorMsg.classList.add("hidden");
    errorMsg.textContent = "";

    const car = {
        owner: loggedUser.username,
        brand: brand.value,
        model: model.value,
        year: Number(year.value),
        vin: vin.value,
        mileage: String(carMileage.value),
    };

    fetch(`${API_URL}/cars`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(car)
    })
    .then(response => {
        if (!response.ok) {
            errorMsg.textContent = "Всички полета са задължителни!";
            errorMsg.classList.remove("hidden");
            throw new Error("Add vehicle error!");
        }
        alert("Успешно добавено превозно средство");
        loadCars();
        showSection("carsPage");
    })
}

function addMaintenance() {
    const carId = maintenanceCarSelect.value;
    const km = Number(mileage.value);
    const desc = buildDescription();

    const manualNext = nextServiceAt.value;
    const autoNext = nextServiceAtMatcher(desc, km);

    const record = {
        serviceDate: serviceDate.value,
        description: desc,
        mileage: km,
        cost: Number(cost.value),
        nextServiceAt: manualNext ? Number(manualNext) : autoNext
    };

    fetch(`${API_URL}/maintenance/${carId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(record)
    })
    .then(() => {
        alert("Успешно добавена сервизна история");
        //resetAddMaintenanceForm(); // reset input fields
        showSection("maintenancePage");
        loadMaintenanceForSelectedCar();
    });
}


/* ===================== DELETE ===================== */
function deleteCar(carId) {
    if (!confirm("Are you sure you want to delete this car?")) return;

    fetch(`${API_URL}/cars/${carId}`, { method: "DELETE" })
        .then(res => {
            if (!res.ok) throw new Error("Delete failed");
            alert("Успешно изтрито превозно средство");
            loadCars();
        })
        .catch(err => alert(err.message));
}

/* ===================== LOAD ===================== */
function loadCars() {
    fetch(`${API_URL}/cars`)
        .then(res => res.json())
        .then(data => {
            cars = data;
            renderCarList();
            populateCarSelects();
        });
}

function loadMaintenanceForSelectedCar() {
    const carId = maintenanceViewCarSelect.value;
    const list = document.getElementById("maintenanceList");

    if (!carId) {
        list.innerHTML = `<tr><td colspan="5">Няма записи</td></tr>`;
        return;
    }

    fetch(`${API_URL}/maintenance/${carId}`)
        .then(res => res.json())
        .then(data => {
            console.log("Maintenance data:", data);

            list.innerHTML = "";

            if (!data.length) {
                list.innerHTML = `<tr><td colspan="5">Няма записи</td></tr>`;
                return;
            }

            data.sort((a, b) => new Date(b.serviceDate) - new Date(a.serviceDate));

            data.forEach(m => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${m.serviceDate}</td>
                    <td>${m.description}</td>
                    <td>${m.mileage} км</td>
                    <td>${m.cost} €</td>
                    <td>${m.nextServiceAt ?? "-"} км</td>
                `;
                list.appendChild(row);
            });
        })
        .catch(err => console.error(err));
}

/* ===================== QUICK INFO ===================== */
function loadQuickInfo() {
    const carId = quickInfoCarSelect.value;
    const grid = document.getElementById("quickInfoGrid");
    const mileageBox = document.getElementById("currentMileage");
    const mileageValue = document.getElementById("currentMileageValue");

    if (!carId) {
        mileageBox.classList.add("hidden");
        renderQuickCards({});
        return;
    }

    // 👉 find selected car from cars array
    const car = cars.find(c => c.id === carId);
    if (car && car.mileage) {
        mileageValue.textContent = car.mileage;
        mileageBox.classList.remove("hidden");
    } else {
        mileageBox.classList.add("hidden");
    }

    /* show/hide update button based on owner */
    const btn = document.getElementById("updateMileageBtn");
    if (car.owner == loggedUser.username) {
        btn.classList.remove("hidden");
    } else {
        btn.classList.add("hidden");
    }

    fetch(`${API_URL}/maintenance/${carId}`)
        .then(res => res.json())
        .then(data => {
            const latest = {};

            data.forEach(m => {
                const detected = detectServices(m.description);

                detected.forEach(type => {
                    if (!latest[type] || m.mileage > latest[type].mileage) {
                        latest[type] = m;
                    }
                });
            });

            renderQuickCards(latest);
        });
}

function nextServiceAtMatcher(description, changedKM) {
    if (!description || !changedKM) return null;

    const normalized = description.toLowerCase();

    const RULES = [
        {
            keywords: ["масло двигател", "engine oil", "oil change"],
            interval: 10000
        },
        {
            keywords: ["маслен филтър", "oil filter"],
            interval: 10000
        },
        {
            keywords: ["въздушен филтър", "air filter", "филтър купе", "cabin filter"],
            interval: 20000
        },
        {
            keywords: ["горивен филтър", "fuel filter"],
            interval: 30000
        },
        {
            keywords: ["масло скоростна кутия", "gearbox oil"],
            interval: 60000
        }
    ];

    let maxInterval = null;

    for (const rule of RULES) {
        if (rule.keywords.some(k => normalized.includes(k))) {
            if (maxInterval === null || rule.interval > maxInterval) {
                maxInterval = rule.interval;
            }
        }
    }

    return maxInterval ? Number(changedKM) + maxInterval : null;
}

function renderQuickCards(latest) {
    const grid = document.getElementById("quickInfoGrid");
    grid.innerHTML = "";

    const labels = {
        oil: "🛢 Масло",
        oilFilter: "🔧 Маслен филтър",
        airFilter: "🌬 Въздушен филтър",
        fuelFilter: "⛽ Горивен филтър",
        cabinFilter: "🧼 Филтър купе",
        brakeDiscs: "🛑 Спирачни дискове",
        brakePads: "🛞 Спирачни накладки",
        gearboxOil: "⚙ Масло скоростна кутия",
    };

    Object.entries(labels).forEach(([key, label]) => {
        const card = document.createElement("div");
        card.className = "quick-card";

        if (latest[key]) {
            card.innerHTML = `
                <strong>${label}</strong><br><br>
                <strong>Дата:</strong> ${latest[key].serviceDate}<br>
                <strong>Пробег:</strong> ${latest[key].mileage} км.<br>
                <strong>Смяна:</strong> ${latest[key].nextServiceAt} км.
            `;
        } else {
            card.innerHTML = `
                <strong>${label}</strong><br>
                Няма данни
            `;
        }
        grid.appendChild(card);
    });
}

/* ===================== CARS LIST ===================== */
function renderCarList() {
    const list = document.getElementById("carList");
    list.innerHTML = "";

    cars
        .slice() // 🔒 avoid mutating original array
        .sort((a, b) => {
            const aIsMine = a.owner === loggedUser.username;
            const bIsMine = b.owner === loggedUser.username;

            return bIsMine - aIsMine; // my cars first
        })
        .forEach(car => {
            const li = document.createElement("li");
            li.className = "car-item";

            const info = document.createElement("div");
            info.className = "car-info";
            info.innerHTML = `
                <span class="car-title">${car.brand} ${car.model} (${car.year})</span>
                <span class="car-owner">👤 ${car.owner ?? "Unknown"}</span>
            `;

            li.appendChild(info);

            // ✅ Delete button ONLY for owner
            if (car.owner === loggedUser.username) {
                const delBtn = document.createElement("button");
                delBtn.className = "delete-btn";
                delBtn.textContent = "🗑 Премахни";
                delBtn.onclick = e => {
                    e.stopPropagation();
                    deleteCar(car.id);
                };

                li.appendChild(delBtn);
            }

            list.appendChild(li);
        });
}

/* ===================== SELECT HELPERS ===================== */
function populateCarSelect(
    select,
    includeDefault = true,
    defaultText = "Избери МПС",
    restrictToOwner = false
) {
    if (!select) return;

    select.innerHTML = "";

    if (includeDefault) {
        const opt = document.createElement("option");
        opt.value = "";
        opt.textContent = defaultText;
        select.appendChild(opt);
    }

    cars.forEach(car => {
        const opt = document.createElement("option");
        opt.value = car.id;

        const isOwner = car.owner === loggedUser.username;
        
        opt.textContent = `${car.brand} ${car.model}`;

        if (restrictToOwner && !isOwner) {
            //opt.disabled = true;
            //opt.style.color = "#9ca3af";
            opt.hidden = true;
        }

        select.appendChild(opt);
    });
}

function populateCarSelects() {
    // note: function populateCarSelect(select: any, includeDefault?: boolean, defaultText?: string, restrictToOwner?: boolean): void

    // View maintenance tab
    populateCarSelect( maintenanceViewCarSelect, true, "Избери МПС", true );
    // Add maintenance tab
    populateCarSelect( maintenanceCarSelect, true, "Избери МПС", true );
    // Quick info tab
    populateCarSelect( quickInfoCarSelect, true, "Избери МПС", true );
}

/* update mileage */
function openMileageModal() {
    document.getElementById("newMileageInput").value = "";
    document.getElementById("mileageModal").classList.remove("hidden");
}

function closeMileageModal() {
    document.getElementById("mileageModal").classList.add("hidden");
}

function submitMileageUpdate() {
    const carId = quickInfoCarSelect.value;
    const newMileage = document.getElementById("newMileageInput").value;

    if (!newMileage || newMileage <= 0) {
        alert("Моля въведи валидни километри");
        return;
    }

    //fetch(`${API_URL}/cars/${selectedCarId}/mileage`, {
    fetch(`${API_URL}/cars/${carId}/mileage`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            mileage: Number(newMileage)
        })
    })
    .then(res => {
        if (!res.ok) {
            throw new Error("Failed to update mileage");
        }
        return res.json();
    })
    .then(data => {
        // update UI
        document.getElementById("currentMileageValue").textContent = data.mileage;
        loadCars(); // reload data
        closeMileageModal();
        alert("Километрите са обновени успешно");
    })
    .catch(err => {
        console.error(err);
        alert("Грешка при обновяване");
    });
}
