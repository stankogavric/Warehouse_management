import flask, json
from flask import Flask, request

app = Flask('__main__', template_folder="", static_folder="", root_path="", static_url_path="")
app.config['TEMPLATES_AUTO_RELOAD'] = True
msgs = []

@app.route('/')
def index_page():
    return ("Hello")

@app.route('/json/<number>')
def prikaz_jednog(number=None):
    skladista = []
    try:
        with open("roba.json", encoding='utf-8') as f:
            data = json.load(f)
            for el in data:
                if el['idSkladista'] == number:
                    skladista.append(el)
            return json.dumps(skladista)
    except(Exception):
        return "Greška"

@app.route('/json')
def ret_json():
    return flask.render_template("skladista.json")

@app.route('/roba')
def ret_roba():
    return flask.render_template("roba.json")

@app.route('/korisnici')
def ret_korisnici():
    return flask.render_template("korisnici.json")

@app.route('/add', methods=['POST'])
def add_new():
    estate = flask.request.form
    data = []
    try:
        with open("roba.json", 'r', encoding='utf-8') as f:
            data = json.load(f)
            data.append(estate)

        with open("roba.json", 'w', encoding='utf-8') as of:
            json.dump(data, of, ensure_ascii=False)
            return "OK"
    except(Exception):
        print(Exception)
        return "Greška"

@app.route('/change', methods=['POST'])
def add_changed():
    estate = flask.request.form
    data = []
    try:
        with open("roba.json", 'r', encoding='utf-8') as f:
            data = json.load(f)
            for d in data:
                if d["id"]==estate["id"]:
                    #d["idSkladista"]=estate["idSkladista"]
                    #d["naziv"]=estate["naziv"]
                    #d["tip"]=estate["tip"]
                    #d["tezina"]=estate["tezina"]
                    d["kolicina"]=estate["kolicina"]
                    d["napomena"]=estate["napomena"]

        with open("roba.json", 'w', encoding='utf-8') as of:
            json.dump(data, of, ensure_ascii=False)
            return "OK"
    except(Exception):
        print(str(Exception))
        return "Greška"

@app.route('/delete', methods=['POST'])
def delete_roba():
    estate = flask.request.form
    data = []
    try:
        with open("roba.json", 'r', encoding='utf-8') as f:
            data = json.load(f)
            for i, d in enumerate (data):
                if d["id"]==estate["id"]:
                    data.remove(data[i])

        with open("roba.json", 'w', encoding='utf-8') as of:
            json.dump(data, of, ensure_ascii=False)
            return "OK"
    except(Exception):
        print(Exception)
        return "Greška"
            
app.run("0.0.0.0", 5000)
