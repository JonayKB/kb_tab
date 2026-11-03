# Spark-REST

A lightweight Forge mod for **Minecraft 1.20.1**, created by **JonayKB**, that exposes server performance metrics from the **Spark** profiler through a simple REST API.

This mod is intended for server administrators who want to monitor TPS, MSPT, CPU usage, and more using external dashboards or monitoring tools.

---

## 📌 Features

* Exposes Spark performance data over HTTP.
* Returns TPS (10s, 1m, 5m, 15m), MSPT, and CPU usage.
* Customizable port and endpoint.
* Optional enable/disable through configuration.
* Extremely lightweight and safe to use.

---

## 📦 Requirements

* **Minecraft Forge 1.20.1**
* **Spark mod** installed on the server
* Java 17+

> ⚠️ Spark-REST does *not* include Spark. You must install Spark separately.

---

## ⚙️ Installation

1. Download `spark-rest.jar`.
2. Place it in your server's `/mods/` folder.
3. Ensure that **Spark** is also installed.
4. Start the server to generate the configuration file.

---

## 📝 Configuration

After the first launch, a config file is generated at:

```
config/spark_rest-common.toml
```

### Example:

```
[general]
port = 8080
endpoint = "metrics"
enabled = true
```

### Options

| Setting    | Description                        | Default |
| ---------- | ---------------------------------- | ------- |
| `port`     | Port on which the HTTP server runs | 8080    |
| `endpoint` | Path used for exposing the metrics | metrics |
| `enabled`  | Enables or disables the REST API   | true    |

---

## 🔗 REST API Usage

Once the server is running, Spark-REST exposes metrics at:

```
http://<your-server-ip>:<port>/<endpoint>
```

### Example:

```
http://localhost:8080/metrics
```

### Example JSON Response:

```json
{
  "tps_10s": 20.0,
  "tps_1m": 19.95,
  "tps_5m": 19.87,
  "tps_15m": 19.76,
  "mspt_1m": 12.4,
  "cpu": 45.2
}
```

---

## ❓ FAQ

### **Q: Spark-REST returns an error / no metrics**

A: Ensure the **Spark** mod is installed. Spark-REST depends on Spark's API.

### **Q: The port is already in use**

A: Change the `port` value in the config file.

---

## 🧑‍💻 Author

**JonayKB**

If you enjoy the mod, feel free to report issues or suggest improvements!

---

## 📄 License
[License](LICENSE)
# kb_tab
