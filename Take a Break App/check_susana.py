#!/usr/bin/env python3
import sqlite3

db_path = r'd:/Projeto de Informática/Take a Break App/data/AgendamentoFolgas.db'
con = sqlite3.connect(db_path)
cur = con.cursor()

# List all tables
tables = cur.execute("SELECT name FROM sqlite_master WHERE type='table'").fetchall()
print(f"SQLite tables: {tables}")

# Find Susana
susana = cur.execute("SELECT * FROM [Funcionário] WHERE Nome LIKE '%Susana%'").fetchone()
print(f"Susana record: {susana}")

if susana:
    susana_id = susana[0]
    folgas_count = cur.execute("SELECT COUNT(*) FROM Folgas WHERE FuncionarioId=?", (susana_id,)).fetchone()[0]
    print(f"Susana ({susana_id}) folgas count in SQLite: {folgas_count}")
    
    if folgas_count > 0:
        folgas = cur.execute("SELECT FolgaId, DataInicio, DataFim, Estado FROM Folgas WHERE FuncionarioId=?", (susana_id,)).fetchall()
        for folga in folgas:
            print(f"  Folga: {folga}")

con.close()
